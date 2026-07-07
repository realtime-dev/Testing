package com.example.authdemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                        Model model,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String registered) {

        // Pull any failure message and attempted username from the session (set by failure handler)
        Object loginError = request.getSession().getAttribute("LOGIN_ERROR_MESSAGE");
        if (loginError != null) {
            model.addAttribute("loginError", loginError.toString());
            request.getSession().removeAttribute("LOGIN_ERROR_MESSAGE");
        }

        Object attempted = request.getSession().getAttribute("LOGIN_ATTEMPTED_USERNAME");
        if (attempted != null) {
            model.addAttribute("lastUsername", attempted.toString());
            request.getSession().removeAttribute("LOGIN_ATTEMPTED_USERNAME");
        }

        if (logout != null) model.addAttribute("logoutParam", true);
        if (registered != null) model.addAttribute("registered", true);

        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        return "dashboard";
    }
}