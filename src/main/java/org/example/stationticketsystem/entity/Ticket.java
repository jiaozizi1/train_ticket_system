package org.example.stationticketsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketNumber;
    private String seatNumber;
    private String status = "ACTIVE";

    // 乘车人信息
    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "passenger_id_card")
    private String passengerIdCard;

    @Column(name = "passenger_phone")
    private String passengerPhone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @Column(name = "purchase_time")
    private LocalDateTime purchaseTime = LocalDateTime.now();

    @Column(name = "refund_time")
    private LocalDateTime refundTime;
}