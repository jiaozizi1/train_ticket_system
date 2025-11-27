package org.example.train_ticket_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 访问 http://localhost:8080 直接跳登录页
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // 管理员专属页面（测试用）
    @GetMapping("/admin/hello")
    public String adminHello() {
        return "redirect:/home";
    }
}