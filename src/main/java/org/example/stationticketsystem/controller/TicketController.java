package org.example.stationticketsystem.controller;

import org.example.stationticketsystem.dto.TicketPurchaseDTO;
import org.example.stationticketsystem.entity.Ticket;
import org.example.stationticketsystem.entity.Train;
import org.example.stationticketsystem.entity.User;
import org.example.stationticketsystem.service.TicketService;
import org.example.stationticketsystem.service.TrainService;
import org.example.stationticketsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TrainService trainService;
    private final UserService userService;
    @GetMapping("/refund/{ticketId}")
    public String refundTicket(@PathVariable("ticketId") Long ticketId,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            System.out.println("=== 开始退票流程 ===");
            System.out.println("退票ID：" + ticketId);
            System.out.println("当前用户：" + (userDetails != null ? userDetails.getUsername() : "未登录"));

            if (userDetails == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "请先登录");
                return "redirect:/login";
            }

            // 获取当前用户
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            System.out.println("用户存在：" + userOpt.isPresent());

            if (userOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "用户不存在");
                return "redirect:/tickets/my-tickets";
            }
            User currentUser = userOpt.get();

            // 获取车票
            System.out.println("查询车票ID：" + ticketId);
            Ticket ticket = ticketService.getTicketById(ticketId);
            System.out.println("车票信息：" + ticket);
            System.out.println("车票状态：" + ticket.getStatus());
            System.out.println("车票所属用户ID：" + ticket.getUser().getId());
            System.out.println("当前用户ID：" + currentUser.getId());

            // 权限检查
            if (!ticket.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("无权操作他人车票");
            }

            // 状态检查
            if (!"ACTIVE".equals(ticket.getStatus())) {
                throw new RuntimeException("该车票已退票或无效");
            }

            // 执行退票
            ticketService.refundTicket(ticket);
            System.out.println("退票成功");

            redirectAttributes.addFlashAttribute("successMessage", "退票成功！");

        } catch (Exception e) {
            System.err.println("退票错误：" + e.getMessage());
            e.printStackTrace(); // 打印完整异常堆栈
            redirectAttributes.addFlashAttribute("errorMessage", "退票失败：" + e.getMessage());
        }

        return "redirect:/tickets/my-tickets";
    }
    // 进入购票表单页面
    @GetMapping("/buy/{trainId}")
    public String showPurchaseForm(@PathVariable Long trainId,
                                   @RequestParam(required = false) String error,
                                   Model model) {
        Optional<Train> trainOpt = trainService.findById(trainId);
        if (trainOpt.isEmpty()) {
            model.addAttribute("errorMessage", "车次不存在");
            return "redirect:/trains";
        }

        TicketPurchaseDTO purchaseDTO = new TicketPurchaseDTO();
        purchaseDTO.setTrainId(trainId);

        // 如果有错误参数，传递错误信息
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }

        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("train", trainOpt.get());
        return "purchase-ticket";
    }

    // 提交购票信息
    @PostMapping("/purchase")
    public String submitPurchaseForm(@Valid @ModelAttribute TicketPurchaseDTO purchaseDTO,
                                     BindingResult bindingResult,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {
        // 获取车次信息
        Optional<Train> trainOpt = trainService.findById(purchaseDTO.getTrainId());
        if (trainOpt.isEmpty()) {
            bindingResult.reject("train.not.found", "车次不存在");
        }

        // 验证表单
        if (bindingResult.hasErrors()) {
            model.addAttribute("train", trainOpt.orElse(null));
            return "purchase-ticket";
        }

        // 进入支付页面
        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("train", trainOpt.get());
        model.addAttribute("verificationCode", generateVerificationCode()); // 生成验证码
        return "payment";
    }

    // 完成支付


    // 辅助方法：生成验证码
    private String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 900000 + 100000));
    }

    // 辅助方法：身份证号脱敏
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    // 其他原有方法...
    @GetMapping("/my-tickets")
    public String myTickets(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            if (userOpt.isPresent()) {
                model.addAttribute("tickets", ticketService.getUserTickets(userOpt.get()));
            }
            return "my-tickets";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "my-tickets";
        }
    }
    @PostMapping("/complete-payment")
    public String completePayment(@ModelAttribute TicketPurchaseDTO purchaseDTO,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== 开始购票流程 ===");
            System.out.println("用户信息：" + (userDetails != null ? userDetails.getUsername() : "未登录"));
            System.out.println("车次ID：" + purchaseDTO.getTrainId());
            System.out.println("乘车人：" + purchaseDTO.getRealName());

            if (userDetails == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "请先登录");
                return "redirect:/login";
            }

            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            Optional<Train> trainOpt = trainService.findById(purchaseDTO.getTrainId());

            System.out.println("用户存在：" + userOpt.isPresent());
            System.out.println("车次存在：" + trainOpt.isPresent());

            if (userOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "用户不存在");
                return "redirect:/login";
            }

            if (trainOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "车次不存在");
                return "redirect:/trains";
            }

            // 创建车票
            Ticket ticket = ticketService.buyTicket(userOpt.get(), trainOpt.get(), purchaseDTO);

            redirectAttributes.addFlashAttribute("successMessage",
                    "购票成功！车票编号：" + ticket.getTicketNumber() +
                            "<br>乘车人：" + purchaseDTO.getRealName() +
                            "<br>身份证号：" + maskIdCard(purchaseDTO.getIdCard()));

            return "redirect:/tickets/my-tickets";

        } catch (Exception e) {
            // 打印详细错误信息
            System.err.println("购票错误：" + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("errorMessage", "购票失败：" + e.getMessage());
            return "redirect:/tickets/buy/" + purchaseDTO.getTrainId();
        }
    }
}