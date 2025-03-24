package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Operator;
import org.lessons.java.ticketplatform.model.Note;
import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.enums.TicketStatus;
import org.lessons.java.ticketplatform.repository.NoteRepository;
import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.security.DatabaseOperatorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;


@Controller
@PreAuthorize("hasRole('OPERATOR')")
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @GetMapping("/dashboard")
    public String operatorDashboard(@AuthenticationPrincipal DatabaseOperatorDetails loggedUser,Model model) {
        model.addAttribute("myTickets", ticketRepository.findByOperator_Id(loggedUser.getId()));
        model.addAttribute("user", loggedUser);
        return "user/dashboard"; // stesso file
    }

    @GetMapping("/tickets/{id}")
    public String showTicket(@PathVariable Integer id, @AuthenticationPrincipal DatabaseOperatorDetails loggedUser, Model model) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            Ticket ticket = result.get();

            // sicurezza: ticket assegnato a lui?
            if (!ticket.getOperator().getId().equals(loggedUser.getId())) {
                return "redirect:/operator/dashboard";
            }

            model.addAttribute("ticket", ticket);
            model.addAttribute("note", new Note()); // per form nuova nota
            return "user/tickets/show";
        }

        return "redirect:/operator/dashboard";
    }

    @PostMapping("/tickets/{id}/notes")
    public String addNote(
        @PathVariable Integer id,
        @ModelAttribute("note") @Valid Note note,
        BindingResult bindingResult,
        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser
    ) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            Ticket ticket = result.get();

            if (!ticket.getOperator().getId().equals(loggedUser.getId())) {
                return "redirect:/operator/dashboard";
            }

            if (bindingResult.hasErrors()) {
                return "redirect:/operator/tickets/" + id;
            }

            note.setTicket(ticket);
            note.setOperator(operatorRepository.findById(loggedUser.getId()).get());
            noteRepository.save(note);

            return "redirect:/operator/tickets/" + id;
        }

        return "redirect:/operator/dashboard";
    }

    @PostMapping("/tickets/{id}/status")
    public String updateStatus(
        @PathVariable Integer id,
        @RequestParam TicketStatus status,
        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser
    ) {
        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            Ticket ticket = result.get();

            if (!ticket.getOperator().getId().equals(loggedUser.getId())) {
                return "redirect:/operator/dashboard";
            }

            ticket.setStatus(status);
            ticketRepository.save(ticket);

            return "redirect:/operator/tickets/" + id;
        }

        return "redirect:/operator/dashboard";
    }

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
