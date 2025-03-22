package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer>{

}
