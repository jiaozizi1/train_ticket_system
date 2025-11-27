package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.entity.User;
import org.example.train_ticket_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        // 修正：单独接收参数并校验确认密码（避免User实体无confirmPassword字段导致的错误）
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMsg", "两次密码不一致");
            return "redirect:/register";
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // 密码加密在Service中处理
            userService.register(user);
            redirectAttributes.addFlashAttribute("successMsg", "注册成功！请登录");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "注册失败：" + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }
}