package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {
    // 继承JpaRepository的findById方法
}