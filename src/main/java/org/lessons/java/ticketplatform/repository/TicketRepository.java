package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TicketRepository extends JpaRepository<Ticket, Integer>{
    List<Ticket> findByTitleContainingIgnoreCase(String title);
    List<Ticket> findByOperator_Id(Integer operatorId);
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.notes WHERE t.id = :id")
    Optional<Ticket> findByIdWithNotes(@Param("id") Integer id);
}
