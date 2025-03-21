package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.enums.Role;
import org.lessons.java.ticketplatform.model.Operator;
import org.lessons.java.ticketplatform.repository.OperatorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    @Autowired
    private OperatorRepository operatorRepository;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/redirect")
    public String redirectAfterLogin(@AuthenticationPrincipal Operator loggedOperator){
        if (loggedOperator == null) {
            return "redirect:/login?error";
        }
    
        if (loggedOperator.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/operator/dashboard";
        }
        }
}
