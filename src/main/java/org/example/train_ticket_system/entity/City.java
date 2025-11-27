package org.example.train_ticket_system.entity;

import org.example.train_ticket_system.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_city")
public class City extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String name;
}