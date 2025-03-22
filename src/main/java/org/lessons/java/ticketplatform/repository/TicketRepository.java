package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Integer>{
    List<Ticket> findByTitleContainingIgnoreCase(String title);
}
