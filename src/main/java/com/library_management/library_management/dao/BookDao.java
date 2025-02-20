package com.library_management.library_management.dao;

import com.library_management.library_management.model.Book;
import com.library_management.library_management.projection.BookProjection;
import com.library_management.library_management.projection.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDao extends JpaRepository<Book, Integer> {

    Optional<Book> findBybookName(String book_name);

//    category.catId: The catId field is part of the Category entity, so you access it through the category field using the underscore _ syntax (category_CatId).
    Optional<List<Book>> findByCategory_catId(Integer id);

}
