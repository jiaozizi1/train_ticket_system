package org.example.train_ticket_system.entity;

import org.example.train_ticket_system.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_seat_type")
public class SeatType extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String name; // 商务座/一等座/二等座

    @Column(nullable = false)
    private Double priceRate = 1.0; // 票价倍率
}