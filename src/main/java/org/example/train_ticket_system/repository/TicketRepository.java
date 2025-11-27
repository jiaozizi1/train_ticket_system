package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // 根据出发站、到达站查询车票
    List<Ticket> findByStartStationAndEndStation(String startStation, String endStation);
}