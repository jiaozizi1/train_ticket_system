package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.entity.Ticket;
import org.example.train_ticket_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    // 车票查询页面
    @GetMapping("/search")
    public String showSearchPage() {
        return "ticket-search";
    }

    // 处理查询请求
    @PostMapping("/search")
    public String searchTickets(
            @RequestParam String startStation,
            @RequestParam String endStation,
            Model model) {

        List<Ticket> tickets = ticketRepository.findByStartStationAndEndStation(startStation, endStation);
        model.addAttribute("tickets", tickets);
        model.addAttribute("startStation", startStation);
        model.addAttribute("endStation", endStation);
        return "ticket-result";
    }
}