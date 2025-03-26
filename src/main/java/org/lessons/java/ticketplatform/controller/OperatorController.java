package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Operator;
import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.enums.TicketStatus;
import org.lessons.java.ticketplatform.repository.NoteRepository;
import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.repository.CategoryRepository;
import org.lessons.java.ticketplatform.security.DatabaseOperatorDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

//import java.util.Optional;
import java.util.List;


@Controller
@PreAuthorize("hasRole('OPERATOR')")
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @GetMapping("/dashboard")
    public String operatorDashboard(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) TicketStatus status,
            @AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
            Model model) {
    
        List<Ticket> filteredTickets = ticketRepository.findByOperator_Id(loggedUser.getId())
            .stream()
            .filter(t -> keyword == null || t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .filter(t -> categoryId == null || (t.getCategory() != null && t.getCategory().getId().equals(categoryId)))
            .filter(t -> status == null || t.getStatus().equals(status))
            .toList();
    
        model.addAttribute("myTickets", filteredTickets);
        model.addAttribute("user", loggedUser);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("status", status);
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("categories", categoryRepository.findAll());
    
        return "operator/dashboard";
    }
    
    /***** CRUD USER *****/
    //showProfile, updateProfile
    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal DatabaseOperatorDetails loggedUser) {
        Operator operator = operatorRepository.findById(loggedUser.getId()).get();
        model.addAttribute("operator", operator);
        return "operator/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
        @ModelAttribute("operator") Operator operatorUpdate,
        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser
    ) {
        Operator operator = operatorRepository.findById(loggedUser.getId()).get();

        // controlla questo controllo davideeeeeeee
        boolean hasPending = ticketRepository
            .findByOperator_Id(operator.getId())
            .stream()
            .anyMatch(t -> t.getStatus() == TicketStatus.TO_DO || t.getStatus() == TicketStatus.ON_GOING);

        if (hasPending && Boolean.TRUE.equals(operatorUpdate.getNotAvailable())) {
            return "redirect:/operator/profile?error=tickets-active";
        }

        operator.setUsername(operatorUpdate.getUsername());
        operator.setNotAvailable(operatorUpdate.getNotAvailable());
        operatorRepository.save(operator);

        return "redirect:/operator/profile?success";
    }
}
