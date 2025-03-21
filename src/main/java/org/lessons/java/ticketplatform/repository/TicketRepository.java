package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Integer>{

}
