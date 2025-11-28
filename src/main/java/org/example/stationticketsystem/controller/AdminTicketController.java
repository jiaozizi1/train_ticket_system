package org.example.stationticketsystem.controller;

import org.example.stationticketsystem.entity.Ticket;
import org.example.stationticketsystem.entity.User;
import org.example.stationticketsystem.service.TicketService;
import org.example.stationticketsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/tickets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // 仅管理员可访问
public class AdminTicketController {
    private final TicketService ticketService;
    private final UserService userService;

    /**
     * 管理员车票列表页面
     */
    @GetMapping
    public String adminTicketList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            Model model) {

        List<Ticket> tickets;

        // 根据状态筛选
        if (status != null && !status.isEmpty()) {
            if ("ACTIVE".equals(status)) {
                tickets = ticketService.findAllActiveTickets();
            } else if ("REFUNDED".equals(status)) {
                tickets = ticketService.findAllRefundedTickets();
            } else {
                tickets = ticketService.findAllTickets();
            }
        }
        // 根据用户筛选
        else if (userId != null) {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            tickets = ticketService.getUserTickets(user);
        }
        // 全部车票
        else {
            tickets = ticketService.findAllTickets();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("users", userService.findAllUsers()); // 用户列表用于筛选
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedUserId", userId);

        return "admin/ticket-list";
    }

    /**
     * 查看车票详情
     */
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "admin/ticket-detail";
    }

    /**
     * 修改车票状态
     */
    @PostMapping("/{id}/status")
    public String updateTicketStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        try {
            Ticket ticket = ticketService.getTicketById(id);

            if ("REFUNDED".equals(status) && "ACTIVE".equals(ticket.getStatus())) {
                // 如果改为退票状态，执行退票逻辑
                ticketService.refundTicket(ticket);
                redirectAttributes.addFlashAttribute("successMessage", "车票已标记为退票");
            }
            else if ("ACTIVE".equals(status) && "REFUNDED".equals(ticket.getStatus())) {
                // 如果恢复为有效状态，更新状态但不恢复余票（管理员手动操作）
                ticket.setStatus("ACTIVE");
                ticket.setRefundTime(null);
                ticketService.saveTicket(ticket);
                redirectAttributes.addFlashAttribute("successMessage", "车票已恢复为有效状态");
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "状态修改无效");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "修改失败：" + e.getMessage());
        }

        return "redirect:/admin/tickets";
    }

    /**
     * 删除车票
     */
    @GetMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketService.deleteTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "车票已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败：" + e.getMessage());
        }

        return "redirect:/admin/tickets";
    }
}