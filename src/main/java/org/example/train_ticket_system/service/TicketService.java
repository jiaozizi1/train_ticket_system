// TicketService.java (新增)
package org.example.train_ticket_system.service;

import org.example.train_ticket_system.entity.TicketRemain;
import org.example.train_ticket_system.entity.TicketRemainPK;
import org.example.train_ticket_system.repository.TicketRemainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    @Autowired
    private TicketRemainRepository ticketRemainRepository;

    @Transactional
    public boolean reduceStock(TicketRemainPK pk) {
        TicketRemain remain = ticketRemainRepository.findById(pk)
                .orElseThrow(() -> new RuntimeException("余票记录不存在"));

        if (remain.getRemain() <= 0) {
            return false;
        }

        remain.setRemain(remain.getRemain() - 1);
        ticketRemainRepository.save(remain);
        return true;
    }
}