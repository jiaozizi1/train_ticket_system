package org.example.stationticketsystem.service;

import org.example.stationticketsystem.dto.TicketPurchaseDTO;
import org.example.stationticketsystem.entity.Ticket;
import org.example.stationticketsystem.entity.Train;
import org.example.stationticketsystem.entity.User;
import org.example.stationticketsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TrainService trainService;

    /**
     * 购票方法（带乘车人信息）
     */
    @Transactional
    public Ticket buyTicket(User user, Train train, TicketPurchaseDTO purchaseDTO) {
        try {
            System.out.println("=== 开始购票 ===");
            System.out.println("车次余票：" + train.getAvailableSeats());

            if (train.getAvailableSeats() == null || train.getAvailableSeats() <= 0) {
                throw new RuntimeException("该车次已无余票");
            }

            // 创建车票
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setTrain(train);
            ticket.setTicketNumber(generateTicketNumber());
            ticket.setSeatNumber(generateSeatNumber(train));
            ticket.setStatus("ACTIVE");

            // 设置乘车人信息
            ticket.setPassengerName(purchaseDTO.getRealName());
            ticket.setPassengerIdCard(purchaseDTO.getIdCard());
            ticket.setPassengerPhone(purchaseDTO.getPhoneNumber());
            ticket.setPurchaseTime(LocalDateTime.now());

            // 减少余票
            trainService.decreaseSeats(train.getId());

            Ticket savedTicket = ticketRepository.save(ticket);
            System.out.println("购票成功，车票ID：" + savedTicket.getId());

            return savedTicket;

        } catch (Exception e) {
            System.err.println("购票失败：" + e.getMessage());
            throw e;
        }
    }

    /**
     * 兼容旧版购票方法
     */
    @Transactional
    public Ticket buyTicket(User user, Train train) {
        TicketPurchaseDTO dto = new TicketPurchaseDTO();
        dto.setRealName(user.getUsername());
        dto.setIdCard("未知");
        dto.setPhoneNumber("未知");
        return buyTicket(user, train, dto);
    }

    /**
     * 退票方法
     */
    @Transactional
    public void refundTicket(Ticket ticket) {
        try {
            System.out.println("=== 开始退票 ===");
            System.out.println("当前车票状态：" + ticket.getStatus());

            if (!"ACTIVE".equals(ticket.getStatus())) {
                throw new RuntimeException("该车票已退票或无效，当前状态：" + ticket.getStatus());
            }

            // 更新车票状态
            ticket.setStatus("REFUNDED");
            ticket.setRefundTime(LocalDateTime.now());
            ticketRepository.save(ticket);

            // 恢复余票
            if (ticket.getTrain() != null) {
                trainService.increaseSeats(ticket.getTrain().getId());
                System.out.println("余票已恢复");
            }

            System.out.println("退票成功，车票ID：" + ticket.getId());

        } catch (Exception e) {
            System.err.println("退票失败：" + e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户的所有车票
     */
    public List<Ticket> getUserTickets(User user) {
        return ticketRepository.findByUser(user);
    }

    /**
     * 根据ID获取车票
     */
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("车票不存在，ID：" + id));
    }

    /**
     * 根据车票编号查询
     */
    public Ticket findByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumber(ticketNumber);
    }

    /**
     * 获取车票统计
     */
    public long getTotalTickets() {
        return ticketRepository.count();
    }

    public long getActiveTickets() {
        return ticketRepository.countByStatus("ACTIVE");
    }

    public long getRefundedTickets() {
        return ticketRepository.countByStatus("REFUNDED");
    }

    /**
     * 生成车票编号
     */
    private String generateTicketNumber() {
        Random random = new Random();
        long timestamp = System.currentTimeMillis();
        int randomNum = random.nextInt(1000);
        return "TK" + timestamp + String.format("%03d", randomNum);
    }

    /**
     * 生成座位号
     */
    private String generateSeatNumber(Train train) {
        Random random = new Random();
        char[] seatTypes = {'A', 'B', 'C', 'D', 'F'}; // 高铁座位类型
        char seatType = seatTypes[random.nextInt(seatTypes.length)];

        int maxSeat = (train.getTotalSeats() != null) ? train.getTotalSeats() : 100;
        int seatNumber = random.nextInt(maxSeat) + 1;

        return seatNumber + String.valueOf(seatType);
    }

    // ========== 新增管理员功能方法 ==========
    /**
     * 查询所有车票（管理员用）
     */
    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * 查询所有有效车票
     */
    public List<Ticket> findAllActiveTickets() {
        return ticketRepository.findByStatus("ACTIVE");
    }

    /**
     * 查询所有已退车票
     */
    public List<Ticket> findAllRefundedTickets() {
        return ticketRepository.findByStatus("REFUNDED");
    }

    /**
     * 保存/更新车票（用于管理员修改状态）
     */
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    /**
     * 删除车票（管理员用）
     */
    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);

        // 如果删除的是有效车票，恢复余票
        if ("ACTIVE".equals(ticket.getStatus()) && ticket.getTrain() != null) {
            trainService.increaseSeats(ticket.getTrain().getId());
        }

        ticketRepository.deleteById(id);
    }
}