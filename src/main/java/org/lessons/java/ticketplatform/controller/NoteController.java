package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Note;
import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.Operator;

import org.lessons.java.ticketplatform.repository.OperatorRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.repository.NoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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

        if (ticketOpt.isPresent()) {
            Note note = new Note();
            note.setContent(content);
            note.setTicket(ticketOpt.get());
            note.setOperator(loggedOperator); // l'autore della nota è l'utente loggato

            noteRepository.save(note);
            return "redirect:/user/tickets/" + ticketId;
        } else {
            return "redirect:/user/dashboard";
        }
    }
}
