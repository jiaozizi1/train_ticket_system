package org.example.train_ticket_system.entity;

import org.example.train_ticket_system.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_order")
public class Order extends BaseEntity {

    @Id
    @Column(length = 30, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_no", referencedColumnName = "trainNo")
    private Train train;

    @Column(nullable = false)
    private String runDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_station_id")
    private Station fromStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_station_id")
    private Station toStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;

    @Column(nullable = false, length = 50)
    private String passengerName;

    @Column(nullable = false, length = 20)
    private String idCard;

    @Column(nullable = false, length = 10)
    private String seatNo;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @Column(nullable = false)
    private String status; // 待支付/已支付/已取消/已退票

    // 状态常量
    public static final String STATUS_PENDING = "待支付";
    public static final String STATUS_PAID = "已支付";
    public static final String STATUS_CANCELLED = "已取消";
    public static final String STATUS_REFUNDED = "已退票";
}