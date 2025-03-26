package org.lessons.java.ticketplatform.controller;

import org.lessons.java.ticketplatform.model.Operator;
import org.lessons.java.ticketplatform.model.Category;
import org.lessons.java.ticketplatform.model.Note;
import org.lessons.java.ticketplatform.model.Ticket;

import org.lessons.java.ticketplatform.repository.NoteRepository;
import org.lessons.java.ticketplatform.repository.TicketRepository;
import org.lessons.java.ticketplatform.repository.CategoryRepository;
import org.lessons.java.ticketplatform.repository.OperatorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import org.lessons.java.ticketplatform.security.DatabaseOperatorDetails;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private CategoryRepository categoryRepository;
                                        /***** CRUDS *****/
    @GetMapping("/tickets/{id}")
    public String show(@PathVariable Integer id,
                        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
                        Model model) {
                            
        Optional<Ticket> result = ticketRepository.findByIdWithNotes(id);
        if(result.isEmpty()){
            return "redirect:/user/dashboard";
        }
            Ticket ticket = result.get();
            boolean isAdmin = loggedUser.getRole().name().equals("ADMIN");
            boolean isOwner = ticket.getOperator().getId().equals(loggedUser.getId());

            if (!isAdmin && !isOwner) {
                return "redirect:/user/dashboard?unauthorized";
            }

            model.addAttribute("ticket", ticket);
            model.addAttribute("note", new Note());
            model.addAttribute("canWrite", isOwner || isAdmin); 
            return "user/tickets/show";

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
            model.addAttribute("operators", operatorRepository.findByNotAvailableFalse());

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

    if (selectedOperator == null || Boolean.TRUE.equals(selectedOperator.getNotAvailable())) {
        if (loggedUser.getRole().name().equals("ADMIN")) {
            return "redirect:/user/tickets/edit/" + id + "?error=operatore-non-disponibile";
        } else {
            return "redirect:/user/tickets/edit/" + id + "?error=operatore-non-disponibile";
        }
    }

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


                                        /**** NOTES ****/
    @PostMapping("/tickets/{id}/notes")
    public String addNote(@PathVariable Integer id,
                        @ModelAttribute("note") @Valid Note note,
                        BindingResult bindingResult,
                        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
                        Model model) {

        Optional<Ticket> result = ticketRepository.findById(id);
        if (result.isPresent()) {
            Ticket ticket = result.get();
            
            if (result.isEmpty()) {
                return "redirect:/user/dashboard";
            }

            if (!ticket.getOperator().getId().equals(loggedUser.getId())) {
                return "redirect:/user/dashboard?unauthorized";
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("ticket", ticket);
                model.addAttribute("note", note); 
                model.addAttribute("canWrite", true);
                return "user/tickets/show";
            }

            note.setId(null); 
            note.setTicket(ticket);
            note.setOperator(operatorRepository.findById(loggedUser.getId()).orElse(null));
            note.setCreatedOn(LocalDate.now());
            noteRepository.save(note);

            return "redirect:/user/tickets/" + id + "?note=success";
        }

        return "redirect:/user/dashboard";
    }
                                        /***** PROFILE *****/
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
                            Model model) {
        Operator operator = operatorRepository.findById(loggedUser.getId()).orElse(null);
        model.addAttribute("operator", operator);
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal DatabaseOperatorDetails loggedUser, Model model) {
        Operator operator = operatorRepository.findById(loggedUser.getId()).orElse(null);
        model.addAttribute("operator", operator);
        return "user/profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
        @ModelAttribute("operator") Operator operatorUpdate,
        @AuthenticationPrincipal DatabaseOperatorDetails loggedUser,
        RedirectAttributes redirectAttributes
    ) {
        Operator operator = operatorRepository.findById(loggedUser.getId()).orElse(null);
        
        if (operator == null) {
            redirectAttributes.addFlashAttribute("error", "Utente non trovato.");
            return "redirect:/login";
        }
    
        boolean hasActiveTickets = ticketRepository
            .findByOperator_Id(operator.getId())
            .stream()
            .anyMatch(t -> t.getStatus().name().equals("TO_DO") || t.getStatus().name().equals("ON_GOING"));
    
        if (Boolean.TRUE.equals(operatorUpdate.getNotAvailable()) && hasActiveTickets) {
            redirectAttributes.addFlashAttribute("error", "Non puoi impostarti come non disponibile se hai ticket attivi.");
            return "redirect:/user/profile/edit";
        }
    
        operator.setUsername(operatorUpdate.getUsername());
    
        if (operatorUpdate.getPassword() != null && !operatorUpdate.getPassword().isBlank()) {
            operator.setPassword(operatorUpdate.getPassword());
            // operator.setPassword(passwordEncoder.encode(operatorUpdate.getPassword()));
        }
    
        operator.setNotAvailable(operatorUpdate.getNotAvailable());
    
        operatorRepository.save(operator);
    
        redirectAttributes.addFlashAttribute("success", "Dati aggiornati con successo.");
        return "redirect:/user/profile";
    }
    




}
