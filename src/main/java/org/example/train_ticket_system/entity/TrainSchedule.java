package org.example.train_ticket_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_train_schedule")
public class TrainSchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainNo;
    private String fromStation;
    private String toStation;
    private String startTime;   // 08:00
    private String endTime;     // 13:36
    private LocalDate runDate;  // 发车日期 ← 新增！

    // 全套 getter/setter（直接用 Lombok 也行，这里手写）
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrainNo() { return trainNo; }
    public void setTrainNo(String trainNo) { this.trainNo = trainNo; }
    public String getFromStation() { return fromStation; }
    public void setFromStation(String fromStation) { this.fromStation = fromStation; }
    public String getToStation() { return toStation; }
    public void setToStation(String toStation) { this.toStation = toStation; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public LocalDate getRunDate() { return runDate; }
    public void setRunDate(LocalDate runDate) { this.runDate = runDate; }
}