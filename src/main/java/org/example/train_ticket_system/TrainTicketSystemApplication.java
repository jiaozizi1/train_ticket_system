package org.example.train_ticket_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication

public class TrainTicketSystemApplication {

    @Autowired
    private TicketRemainRepository ticketRemainRepository;

    public static void main(String[] args) {
        SpringApplication.run(TrainTicketSystemApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initTestData() {
        // 初始化测试余票数据
        TicketRemainPK pk = new TicketRemainPK();
        pk.setTrainNo("G1001");
        pk.setRunDate("2025-12-01");
        pk.setFromStationId(1L);
        pk.setToStationId(2L);
        pk.setSeatTypeId(3L);

        if (!ticketRemainRepository.existsById(pk)) {
            TicketRemain remain = new TicketRemain();
            remain.setTrainNo("G1001");
            remain.setRunDate("2025-12-01");
            remain.setFromStationId(1L);
            remain.setToStationId(2L);
            remain.setSeatTypeId(3L);
            remain.setRemain(10);
            ticketRemainRepository.save(remain);
        }
    }
}