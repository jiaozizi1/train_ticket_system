package org.example.train_ticket_system.entity;

import org.example.train_ticket_system.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_train")
public class Train extends BaseEntity {

    @Column(unique = true, length = 20, nullable = false)
    private String trainNo;

    @Column(nullable = false, length = 20)
    private String type; // 高铁/动车/普通列车

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_station_id")
    private Station endStation;

    private Integer totalSeats; // 总座位数
}