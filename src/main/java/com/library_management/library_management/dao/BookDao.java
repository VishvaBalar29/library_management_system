package com.library_management.library_management.dao;

import com.library_management.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookDao extends JpaRepository<Book, Integer> {

    Optional<Book> findBybookName(String book_name);
}
