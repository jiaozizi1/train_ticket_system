package org.example.train_ticket_system.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class TicketRemain {
    @EmbeddedId // 使用复合主键
    private TicketRemainPK id;    // 关联上面的复合主键类
    private Integer remain;       // 剩余票数
}