package org.example.stationticketsystem.repository;

import org.example.stationticketsystem.entity.Ticket;
import org.example.stationticketsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    Ticket findByTicketNumber(String ticketNumber);
    long countByStatus(String status);

    // 新增方法：按状态查询车票
    List<Ticket> findByStatus(String status);
}