package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
@Slf4j
public class GameController {

    @GetMapping("/init")
    public String init(Model model) {
        String name = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
        log.info("name={}", name);
        if (name.equals("anonymousUser")) {
            return "init";
        }
        model.addAttribute("memberName", name);
        return "init";
    }
}
