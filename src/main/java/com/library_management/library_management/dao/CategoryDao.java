package com.library_management.library_management.dao;


import com.library_management.library_management.model.Category;
import com.library_management.library_management.projection.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(String categoryName);


}
