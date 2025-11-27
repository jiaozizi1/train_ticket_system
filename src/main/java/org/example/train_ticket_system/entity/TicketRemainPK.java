// 文件路径：src/main/java/org/example/train_ticket_system/entity/TicketRemainPK.java
package org.example.train_ticket_system.entity;

import java.io.Serializable;
import java.util.Objects;

public class TicketRemainPK implements Serializable {

    private String trainNo;
    private String runDate;
    private Long fromStationId;
    private Long toStationId;
    private Long seatTypeId;

    // 必须有空构造
    public TicketRemainPK() {}

    // ====== 必须加全套 getter 和 setter ======
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

    // ====== equals 和 hashCode 必须重写（JPA 必备）======
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketRemainPK)) return false;
        TicketRemainPK that = (TicketRemainPK) o;
        return Objects.equals(trainNo, that.trainNo) &&
                Objects.equals(runDate, that.runDate) &&
                Objects.equals(fromStationId, that.fromStationId) &&
                Objects.equals(toStationId, that.toStationId) &&
                Objects.equals(seatTypeId, that.seatTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainNo, runDate, fromStationId, toStationId, seatTypeId);
    }
}