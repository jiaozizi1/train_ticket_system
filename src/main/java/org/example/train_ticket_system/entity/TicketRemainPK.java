package org.example.train_ticket_system.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable // 标记为嵌入式主键
public class TicketRemainPK implements Serializable {
    private String trainNo;       // 车次号
    private String runDate;       // 运行日期（格式：yyyy-MM-dd）
    private Long fromStationId;   // 出发站ID
    private Long toStationId;     // 到达站ID
    private Long seatTypeId;      // 座位类型ID（1-商务座，2-一等座，3-二等座）
}