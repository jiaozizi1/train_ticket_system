package org.example.train_ticket_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_ticket_remain")
@IdClass(TicketRemainPK.class)          // ← 指向下面那个类
public class TicketRemain {

    @Id private String trainNo;
    @Id private String runDate;
    @Id private Long fromStationId;
    @Id private Long toStationId;
    @Id private Long seatTypeId;

    @Column(nullable = false)
    private Integer remain = 999;

    @Version
    private Integer version = 0;

    // getter setter
    public String getTrainNo() { return trainNo; } public void setTrainNo(String trainNo) { this.trainNo = trainNo; }
    public String getRunDate() { return runDate; } public void setRunDate(String runDate) { this.runDate = runDate; }
    public Long getFromStationId() { return fromStationId; } public void setFromStationId(Long fromStationId) { this.fromStationId = fromStationId; }
    public Long getToStationId() { return toStationId; } public void setToStationId(Long toStationId) { this.toStationId = toStationId; }
    public Long getSeatTypeId() { return seatTypeId; } public void setSeatTypeId(Long seatTypeId) { this.seatTypeId = seatTypeId; }
    public Integer getRemain() { return remain; } public void setRemain(Integer remain) { this.remain = remain; }
    public Integer getVersion() { return version; } public void setVersion(Integer version) { this.version = version; }
}