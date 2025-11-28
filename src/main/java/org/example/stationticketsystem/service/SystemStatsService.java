package org.example.stationticketsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemStatsService {
    private final TrainService trainService;
    private final UserService userService;
    private final TicketService ticketService;

    public long getTotalTrains() {
        return trainService.findAllTrains().size();
    }

    public long getTotalUsers() {
        return userService.findAllUsers().size();
    }

    public long getTotalTickets() {
        return ticketService.getTotalTickets();
    }

    public long getAvailableSeats() {
        return trainService.findAllTrains().stream()
                .mapToLong(train -> train.getAvailableSeats())
                .sum();
    }

    public long getTotalSeats() {
        return trainService.findAllTrains().stream()
                .mapToLong(train -> train.getTotalSeats())
                .sum();
    }
}