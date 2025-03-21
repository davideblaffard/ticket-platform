package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Ticket;

import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/admin/dashboard")
    public String dashboard(@RequestParam(required = false) String keyword, Model model) {
        List<Ticket> tickets;

        if (keyword != null && !keyword.isEmpty()) {
            tickets = ticketRepository.findByTitleContainingIgnoreCase(keyword);
        } else {
            tickets = ticketRepository.findAll();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("keyword", keyword);

        return "admin/dashboard";
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
        ticketRepository.save(ticket);
        return "redirect:/admin/dashboard";
    }

    public String show(@PathVariable Integer id, Model model) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("ticket", result.get());
            return "admin/tickets/show";
        } else {
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/tickets/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("ticket", result.get());
            model.addAttribute("operators", operatorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/tickets/edit";
        } else {
            return "redirect:/admin/dashboard";
        }
    }

    @PostMapping("/tickets/edit/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Ticket ticket) {
        ticket.setId(id);
        ticketRepository.save(ticket);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/tickets/delete/{id}")
    public String delete(@PathVariable Integer id) {
        ticketRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}
