package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.enums.Role;
import org.lessons.java.ticketplatform.security.DatabaseOperatorDetails;
import org.lessons.java.ticketplatform.model.Operator;

// import org.lessons.java.ticketplatform.repository.OperatorRepository;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    // @Autowired
    // private OperatorRepository operatorRepository;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/redirect")
    public String redirectAfterLogin(@AuthenticationPrincipal DatabaseOperatorDetails loggedUser) {
        if (loggedUser.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";     // AdminController
        } else {
            return "redirect:/operator/dashboard"; // OperatorController
        }
    }
}
