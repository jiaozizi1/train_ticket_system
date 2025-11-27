package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // 新增：根据出发站和到达站查询车票
    List<Ticket> findByStartStationAndEndStation(String startStation, String endStation);
}