package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Note;
import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.Operator;

import org.lessons.java.ticketplatform.repository.NoteRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.repository.OperatorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @PostMapping("/notes/create")
    public String store(
            @RequestParam("ticketId") Integer ticketId,
            @RequestParam("content") String content,
            @AuthenticationPrincipal Operator loggedOperator) {

        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            return "redirect:/operator/dashboard";
        }

        Ticket ticket = ticketOpt.get();

        // Sicurezza: solo l'operatore assegnato può aggiungere note
        if (!ticket.getOperator().getId().equals(loggedOperator.getId())) {
            return "redirect:/operator/dashboard";
        }

        // Creazione della nota
        Note note = new Note();
        note.setContent(content);
        note.setTicket(ticket);
        note.setOperator(loggedOperator);

        noteRepository.save(note);

        return "redirect:/operator/tickets/" + ticketId;
    }
}
