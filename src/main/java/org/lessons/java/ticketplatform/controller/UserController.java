package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Operator;
import org.lessons.java.ticketplatform.model.Category;
import org.lessons.java.ticketplatform.model.Note;
import org.lessons.java.ticketplatform.model.Ticket;

import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.repository.CategoryRepository;
import org.lessons.java.ticketplatform.repository.OperatorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.lessons.java.ticketplatform.security.DatabaseOperatorDetails;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/tickets/{id}")
    public String show(@PathVariable Integer id,
    @AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
    Model model) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            Ticket ticket = result.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("note", new Note());
            return "user/tickets/show";
        }
        return "redirect:/user/dashboard"; 
    }

    @GetMapping("/tickets/edit/{id}")
    public String edit(@PathVariable Integer id,
    @AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
    Model model) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            Ticket ticket = result.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("operators", operatorRepository.findAll());
            model.addAttribute("formAction", "/user/tickets/edit/" + id);

            return "user/tickets/edit";
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/tickets/edit/{id}")
    public String update(@PathVariable Integer id,
                        @ModelAttribute Ticket ticket,
                        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser) {

    Operator selectedOperator = operatorRepository.findById(ticket.getOperator().getId()).orElse(null);
    Category selectedCategory = categoryRepository.findById(ticket.getCategory().getId()).orElse(null);

    ticket.setId(id); 
    ticket.setOperator(selectedOperator);
    ticket.setCategory(selectedCategory);

    ticketRepository.save(ticket);

    if (loggedUser.getRole().name().equals("ADMIN")) {
        return "redirect:/admin/dashboard?successId=" + id;
    } else {
        return "redirect:/operator/dashboard?successId=" + id;
    }
}


}
