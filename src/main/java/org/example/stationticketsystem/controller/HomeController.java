package org.example.stationticketsystem.controller;

import org.example.stationticketsystem.entity.User;
import org.example.stationticketsystem.service.TrainService;
import org.example.stationticketsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final TrainService trainService;
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            if (userOpt.isPresent()) {
                model.addAttribute("user", userOpt.get());
            }
        }
        return "home";
    }

    @GetMapping("/trains")
    public String trains(Model model) {
        model.addAttribute("trains", trainService.findAllTrains());
        return "trains";
    }

    @PostMapping("/search")
    public String searchTrains(@RequestParam(required = false) String start,
                               @RequestParam(required = false) String end,
                               Model model) {
        if (start == null || end == null || start.isEmpty() || end.isEmpty()) {
            model.addAttribute("trains", trainService.findAllTrains());
        } else {
            model.addAttribute("trains", trainService.searchTrains(start, end));
            model.addAttribute("start", start);
            model.addAttribute("end", end);
        }
        return "trains";
    }
}