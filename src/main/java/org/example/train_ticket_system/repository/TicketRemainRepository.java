package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.TicketRemain;
import org.example.train_ticket_system.entity.TicketRemainPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRemainRepository extends JpaRepository<TicketRemain, TicketRemainPK> {
    // JpaRepository自带findById、save等方法，无需额外定义
}