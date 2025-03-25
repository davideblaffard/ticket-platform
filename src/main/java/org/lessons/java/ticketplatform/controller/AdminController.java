package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.Operator;

import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.security.DatabaseOperatorDetails;
import org.lessons.java.ticketplatform.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Lista e ricerca
    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(required = false) String keyword, Model model, 
    @AuthenticationPrincipal DatabaseOperatorDetails loggedUser) {
        List<Ticket> allTickets = (keyword != null && !keyword.isEmpty())
            ? ticketRepository.findByTitleContainingIgnoreCase(keyword)
            : ticketRepository.findAll();

        List<Ticket> myTickets = ticketRepository.findByOperator_Id(loggedUser.getId());

        // if (keyword != null && !keyword.trim().isEmpty()) {
        //     allTickets = ticketRepository.findByTitleContainingIgnoreCase(keyword);
        // } else {
        //     allTickets = ticketRepository.findAll();
        // }

        // myTickets = ticketRepository.findByOperator_Id(loggedUser.getId());

        model.addAttribute("tickets", allTickets);
        model.addAttribute("keyword", keyword);
        model.addAttribute("user", loggedUser);
        model.addAttribute("myTickets", myTickets);

        return "admin/dashboard";
    }


    @GetMapping("/profile")
    public String showAdminProfile(
        Model model,
        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser
    ) {
        Operator admin = operatorRepository.findById(loggedUser.getId()).get();
        model.addAttribute("admin", admin);
        return "admin/profile";
    }

    @PostMapping("/profile")
    public String updateAdminProfile(
        @ModelAttribute("admin") Operator adminUpdate,
        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser
    ) {
        Operator admin = operatorRepository.findById(loggedUser.getId()).get();

        admin.setUsername(adminUpdate.getUsername());
        operatorRepository.save(admin);

        return "redirect:/admin/profile?success";
    }


    /***** CRUD *****/
    @GetMapping("/tickets/create")
    public String create(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("operators", operatorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/tickets/create";
    }

    @PostMapping("/tickets/create")
    public String store(@ModelAttribute Ticket ticket) {
        Ticket savedTicket = ticketRepository.save(ticket);
        return "redirect:/admin/dashboard?createdId=" + savedTicket.getId();
    }

    @PostMapping("/tickets/delete/{id}")
    public String delete(@PathVariable Integer id) {
        ticketRepository.deleteById(id);
        return "redirect:/admin/dashboard?deletedId=" + id;
    }
}
