// Ticket.java
package org.example.train_ticket_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainNumber; // 车次

    // 修正：确保为String类型（存储车站名称，如"北京西站"）
    @Column(nullable = false)
    private String startStation;

    @Column(nullable = false)
    private String endStation;

    @Column(nullable = false)
    private LocalDateTime departureTime; // 发车时间

    @Column(nullable = false)
    private LocalDateTime arrivalTime; // 到达时间

    private Double price; // 票价

    private Integer remainingTickets; // 余票数量
}