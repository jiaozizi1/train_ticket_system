// 4. Train.java
package org.example.train_ticket_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_train")
public class Train {
    @Id
    @Column(length = 20)
    private String trainNo;

    private String type;
    private Long startStationId;
    private Long endStationId;

    public String getTrainNo() { return trainNo; }
    public void setTrainNo(String trainNo) { this.trainNo = trainNo; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getStartStationId() { return startStationId; }
    public void setStartStationId(Long startStationId) { this.startStationId = startStationId; }
    public Long getEndStationId() { return endStationId; }
    public void setEndStationId(Long endStationId) { this.endStationId = endStationId; }
}