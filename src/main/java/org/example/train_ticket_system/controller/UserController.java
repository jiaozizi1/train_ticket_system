package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.entity.User;
import org.example.train_ticket_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 注册页面
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 注册提交
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           RedirectAttributes redirectAttributes) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                throw new RuntimeException("用户名不能为空");
            }
            userService.register(user);
            redirectAttributes.addFlashAttribute("successMsg", "注册成功！请登录");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "注册失败：" + e.getMessage());
            return "redirect:/register";
        }
    }

    // 登录页面
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 首页（登录后访问）
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }
}