package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Ticket;
import org.lessons.java.ticketplatform.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TicketRepository extends JpaRepository<Ticket, Integer>{
    List<Ticket> findByCategory_Id(Integer categoryId);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByCategory_IdAndStatus(Integer categoryId, TicketStatus status);
    List<Ticket> findByTitleContainingIgnoreCase(String title);
    List<Ticket> findByOperator_Id(Integer operatorId);
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.notes LEFT JOIN FETCH t.operator WHERE t.id = :id")
    Optional<Ticket> findByIdWithNotes(@Param("id") Integer id);
}
