package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Operator;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {
    Optional<Operator> findByEmail(String email);
}
