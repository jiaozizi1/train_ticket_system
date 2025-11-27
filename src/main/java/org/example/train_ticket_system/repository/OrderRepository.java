package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(Long userId);
}