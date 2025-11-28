package org.example.stationticketsystem.repository;

import org.example.stationticketsystem.entity.Ticket;
import org.example.stationticketsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    Ticket findByTicketNumber(String ticketNumber);

    // 添加统计查询方法
    long countByStatus(String status);

    @Query("SELECT COUNT(t) FROM Ticket t")
    long countAllTickets();
}