// 文件路径：train_ticket_system/src/main/java/org/example/train_ticket_system/controller/OrderController.java
package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.entity.Order;
import org.example.train_ticket_system.entity.TicketRemain;
import org.example.train_ticket_system.entity.TicketRemainPK;
import org.example.train_ticket_system.entity.User;
import org.example.train_ticket_system.repository.OrderRepository;
import org.example.train_ticket_system.repository.TicketRemainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketRemainRepository ticketRemainRepository;

    // 我的订单
    @GetMapping("/my")
    public String myOrders(@AuthenticationPrincipal User loginUser, Model model, RedirectAttributes redirectAttributes) {
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录！");
            return "redirect:/login";
        }
        List<Order> orders = orderRepository.findByUserId(loginUser.getId());
        model.addAttribute("orders", orders);
        return "order/list";
    }

    // 下单方法
    @PostMapping("/create")
    public String createOrder(
            @RequestParam String trainNo,
            @RequestParam String runDate,
            @RequestParam Long seatTypeId,
            @RequestParam Long fromStationId,
            @RequestParam Long toStationId,
            @AuthenticationPrincipal User loginUser,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录后购票！");
            return "redirect:/login";
        }

        // 构造余票主键
        TicketRemainPK pk = new TicketRemainPK();
        pk.setTrainNo(trainNo);
        pk.setRunDate(runDate);
        pk.setFromStationId(fromStationId);
        pk.setToStationId(toStationId);
        pk.setSeatTypeId(seatTypeId);

        // 查余票
        TicketRemain remain = ticketRemainRepository.findById(pk)
                .orElseThrow(() -> new RuntimeException("余票记录不存在"));

        // 防超卖
        if (remain.getRemain() <= 0) {
            redirectAttributes.addFlashAttribute("message", "抱歉，余票已售罄！");
            return "redirect:/train/search";
        }

        // 扣库存
        remain.setRemain(remain.getRemain() - 1);
        ticketRemainRepository.save(remain);

        // 创建订单（确保所有setter方法参数类型正确）
        Order order = new Order();
        order.setUserId(loginUser.getId()); // 确保loginUser.getId()返回Long类型
        order.setTrainNo(trainNo);
        order.setRunDate(runDate);
        order.setSeatTypeId(seatTypeId);
        order.setFromStationId(fromStationId);
        order.setToStationId(toStationId);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("已支付");

        orderRepository.save(order);

        model.addAttribute("order", order);
        return "order/success";
    }
}