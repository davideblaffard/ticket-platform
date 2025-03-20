package org.lessons.java.ticketplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String index(Model model){
        List<Ticket> tickets = ticketService.findAll();
        model.addAttribute("tickets", tickets);
        return "tickets/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model){
        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        return "tickets/show";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("ticket", new Ticket());
        return "tickets/create";
    }

    @PostMapping("/create")
    public String store(@ModelAttribute Ticket ticket) {
        ticketService.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        return "tickets/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integre id, @ModelAttribute Ticket ticket){
        ticketService.save(id, ticket);
        return "redirect:/tickets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        ticketService.delete(id);
        return "redirect:/tickets";
    }
}
