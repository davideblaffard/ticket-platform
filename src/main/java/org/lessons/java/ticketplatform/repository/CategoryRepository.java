package org.lessons.java.ticketplatform.repository;

import org.lessons.java.ticketplatform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer>{


}
