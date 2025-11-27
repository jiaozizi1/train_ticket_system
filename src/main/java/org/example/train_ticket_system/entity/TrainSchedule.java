// TrainSchedule.java
package org.example.train_ticket_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_train_schedule")
public class TrainSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainNo;  // 车次编号（String类型）
    private String fromStation;  // 出发站名称（String类型，如"北京西站"）
    private String toStation;    // 到达站名称（String类型，如"上海虹桥站"）
    private String startTime;    // 出发时间（String类型，如"08:00"）
    private String endTime;      // 到达时间（String类型，如"13:36"）
    private LocalDate runDate;   // 发车日期

    // 确保所有setter方法参数类型为String（与字段一致）
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setFromStation(String fromStation) {  // 参数类型为String
        this.fromStation = fromStation;
    }

    public void setToStation(String toStation) {  // 参数类型为String
        this.toStation = toStation;
    }

    public void setStartTime(String startTime) {  // 参数类型为String
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {  // 参数类型为String
        this.endTime = endTime;
    }

    // 其他getter/setter方法（保持不变）
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrainNo() { return trainNo; }
    public String getFromStation() { return fromStation; }
    public String getToStation() { return toStation; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public LocalDate getRunDate() { return runDate; }
    public void setRunDate(LocalDate runDate) { this.runDate = runDate; }
}