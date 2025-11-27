package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.common.Result;
import org.example.train_ticket_system.entity.*;
import org.example.train_ticket_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketRemainRepository ticketRemainRepository;

    @Autowired
    private TrainRepository trainRepository;  // 新增：查询Train实体

    @Autowired
    private StationRepository stationRepository;  // 新增：查询Station实体

    @Autowired
    private SeatTypeRepository seatTypeRepository;  // 新增：查询SeatType实体

    // 我的订单列表
    @GetMapping("/my")
    public String myOrders(
            @AuthenticationPrincipal User loginUser,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录！");
            return "redirect:/login";
        }

        // 修正：根据关联的User实体查询订单（而非userId）
        List<Order> orders = orderRepository.findByUser(loginUser);
        model.addAttribute("orders", orders);
        return "order/list";
    }

    // 创建订单（购票）
    @PostMapping("/create")
    @ResponseBody
    public Result createOrder(
            @RequestParam String trainNo,  // 车次号
            @RequestParam String runDate,  // 运行日期
            @RequestParam Long seatTypeId, // 座位类型ID
            @RequestParam Long fromStationId, // 出发站ID
            @RequestParam Long toStationId,   // 到达站ID
            @RequestParam String passengerName, // 乘客姓名
            @RequestParam String idCard,       // 身份证号
            @AuthenticationPrincipal User loginUser) {

        if (loginUser == null) {
            return Result.error("请先登录后购票");
        }

        try {
            // 1. 校验关联实体是否存在
            Train train = trainRepository.findByTrainNo(trainNo)
                    .orElseThrow(() -> new RuntimeException("车次不存在"));
            Station fromStation = stationRepository.findById(fromStationId)
                    .orElseThrow(() -> new RuntimeException("出发站不存在"));
            Station toStation = stationRepository.findById(toStationId)
                    .orElseThrow(() -> new RuntimeException("到达站不存在"));
            SeatType seatType = seatTypeRepository.findById(seatTypeId)
                    .orElseThrow(() -> new RuntimeException("座位类型不存在"));

            // 2. 校验余票（使用复合主键查询）
            TicketRemainPK remainPk = new TicketRemainPK();
            remainPk.setTrainNo(trainNo);
            remainPk.setRunDate(runDate);
            remainPk.setFromStationId(fromStationId);
            remainPk.setToStationId(toStationId);
            remainPk.setSeatTypeId(seatTypeId);

            TicketRemain ticketRemain = ticketRemainRepository.findById(remainPk)
                    .orElseThrow(() -> new RuntimeException("余票信息不存在"));

            if (ticketRemain.getRemain() <= 0) {
                return Result.error("余票已售罄");
            }

            // 3. 扣减余票
            ticketRemain.setRemain(ticketRemain.getRemain() - 1);
            ticketRemainRepository.save(ticketRemain);

            // 4. 生成订单（适配Order实体的关联关系）
            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString().replace("-", "")); // 订单ID
            order.setUser(loginUser); // 关联当前登录用户
            order.setTrain(train);    // 关联车次
            order.setRunDate(runDate);
            order.setFromStation(fromStation); // 关联出发站
            order.setToStation(toStation);     // 关联到达站
            order.setSeatType(seatType);       // 关联座位类型
            order.setPassengerName(passengerName);
            order.setIdCard(idCard);
            order.setSeatNo(generateSeatNo(trainNo, seatTypeId)); // 生成座位号（示例方法）
            order.setPrice(calculatePrice(fromStation, toStation, seatType)); // 计算票价（示例方法）
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(Order.STATUS_PAID); // 默认为已支付

            orderRepository.save(order);

            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 取消订单
    @PostMapping("/cancel/{orderId}")
    @ResponseBody
    public Result cancelOrder(@PathVariable String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        // 只有已支付状态可以取消
        if (!Order.STATUS_PAID.equals(order.getStatus())) {
            return Result.error("只能取消已支付的订单");
        }

        // 恢复余票
        TicketRemainPK remainPk = new TicketRemainPK();
        remainPk.setTrainNo(order.getTrain().getTrainNo());
        remainPk.setRunDate(order.getRunDate());
        remainPk.setFromStationId(order.getFromStation().getId());
        remainPk.setToStationId(order.getToStation().getId());
        remainPk.setSeatTypeId(order.getSeatType().getId());

        TicketRemain ticketRemain = ticketRemainRepository.findById(remainPk)
                .orElseThrow(() -> new RuntimeException("余票信息不存在"));
        ticketRemain.setRemain(ticketRemain.getRemain() + 1);
        ticketRemainRepository.save(ticketRemain);

        // 更新订单状态
        order.setStatus(Order.STATUS_CANCELLED);
        orderRepository.save(order);

        return Result.success("订单已取消");
    }

    // 生成座位号（示例方法，实际逻辑需根据业务调整）
    private String generateSeatNo(String trainNo, Long seatTypeId) {
        // 简单示例：座位类型ID+随机数
        return seatTypeId + "-" + (int) (Math.random() * 100);
    }

    // 计算票价（示例方法，实际逻辑需根据业务调整）
    private Double calculatePrice(Station from, Station to, SeatType seatType) {
        // 简单示例：根据座位类型和站数计算
        return seatType.getPriceMultiplier() * 50.0; // 假设基础票价50元
    }
}