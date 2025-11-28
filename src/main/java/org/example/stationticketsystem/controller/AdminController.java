package org.example.stationticketsystem.controller;

import org.example.stationticketsystem.entity.Train;
import org.example.stationticketsystem.entity.User;
import org.example.stationticketsystem.service.TrainService;
import org.example.stationticketsystem.service.UserService;
import org.example.stationticketsystem.service.SystemStatsService;
import org.example.stationticketsystem.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TrainService trainService;
    private final UserService userService;
    private final SystemStatsService systemStatsService;
    private final TicketService ticketService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // 获取真实统计数据
        long totalTrains = systemStatsService.getTotalTrains();
        long totalUsers = systemStatsService.getTotalUsers();
        long totalTickets = ticketService.getTotalTickets();
        long activeTickets = ticketService.getActiveTickets();
        long refundedTickets = ticketService.getRefundedTickets();
        long availableSeats = systemStatsService.getAvailableSeats();
        long totalSeats = systemStatsService.getTotalSeats();

        // 计算各种比率
        double seatUsageRate = totalSeats > 0 ? (double)(totalSeats - availableSeats) / totalSeats * 100 : 0;
        double refundRate = totalTickets > 0 ? (double)refundedTickets / totalTickets * 100 : 0;

        model.addAttribute("totalTrains", totalTrains);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("activeTickets", activeTickets);
        model.addAttribute("refundedTickets", refundedTickets);
        model.addAttribute("seatUsageRate", String.format("%.1f", seatUsageRate));
        model.addAttribute("refundRate", String.format("%.1f", refundRate));

        return "admin/dashboard";
    }

    @GetMapping("/trains/add")
    public String addTrainForm(Model model) {
        model.addAttribute("train", new Train());
        return "admin/add-train";
    }

    @PostMapping("/trains/add")
    public String addTrain(@ModelAttribute Train train, Model model) {
        try {
            trainService.addTrain(train);
            return "redirect:/admin/trains?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("train", train);
            return "admin/add-train";
        }
    }

    @GetMapping("/trains")
    public String manageTrains(Model model) {
        model.addAttribute("trains", trainService.findAllTrains());
        return "admin/manage-trains";
    }

    @GetMapping("/trains/delete/{id}")
    public String deleteTrain(@PathVariable Long id) {
        try {
            trainService.deleteTrain(id);
        } catch (Exception e) {
            // 忽略删除错误
        }
        return "redirect:/admin/trains";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/manage-users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // 处理Optional类型
            User user = userService.findById(id).orElse(null);

            // 不允许删除管理员admin
            if (user != null && "admin".equals(user.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "不能删除管理员账号");
                return "redirect:/admin/users";
            }

            userService.deleteUser(id); // 调用UserService的deleteUser方法
            redirectAttributes.addFlashAttribute("successMessage", "用户已删除");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败：" + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/system-status")
    public String systemStatus(Model model) {
        model.addAttribute("totalTrains", systemStatsService.getTotalTrains());
        model.addAttribute("totalUsers", systemStatsService.getTotalUsers());
        model.addAttribute("totalTickets", ticketService.getTotalTickets());
        model.addAttribute("activeTickets", ticketService.getActiveTickets());
        model.addAttribute("refundedTickets", ticketService.getRefundedTickets());
        model.addAttribute("availableSeats", systemStatsService.getAvailableSeats());
        model.addAttribute("totalSeats", systemStatsService.getTotalSeats());
        return "admin/system-status";
    }

    // 添加管理员首页重定向
    @GetMapping
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }
}