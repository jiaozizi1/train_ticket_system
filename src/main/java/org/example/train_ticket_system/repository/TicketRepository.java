// TicketRepository.java
package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // 方法参数类型为String，与Ticket的startStation、endStation字段类型匹配
    List<Ticket> findByStartStationAndEndStation(String startStation, String endStation);
}