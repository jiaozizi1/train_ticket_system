// 文件路径：src/main/java/org/example/train_ticket_system/entity/Order.java
package org.example.train_ticket_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_order")
public class Order {

    @Id
    @Column(length = 30)
    private String orderId;

    private Long userId;
    private String trainNo;
    private String runDate;
    private Long fromStationId;
    private Long toStationId;
    private Long seatTypeId;
    private String passengerName;
    private String idCard;
    private String seatNo;
    private Double price;
    private LocalDateTime orderTime;
    private String status;

    // ========== 全套 getter 和 setter（必须有！）==========
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTrainNo() { return trainNo; }
    public void setTrainNo(String trainNo) { this.trainNo = trainNo; }

    public String getRunDate() { return runDate; }
    public void setRunDate(String runDate) { this.runDate = runDate; }

    public Long getFromStationId() { return fromStationId; }
    public void setFromStationId(Long fromStationId) { this.fromStationId = fromStationId; }

    public Long getToStationId() { return toStationId; }
    public void setToStationId(Long toStationId) { this.toStationId = toStationId; }

    public Long getSeatTypeId() { return seatTypeId; }
    public void setSeatTypeId(Long seatTypeId) { this.seatTypeId = seatTypeId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getSeatNo() { return seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}