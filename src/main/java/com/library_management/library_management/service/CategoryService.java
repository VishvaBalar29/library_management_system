package com.library_management.library_management.service;

import com.library_management.library_management.dao.CategoryDao;
import com.library_management.library_management.model.Category;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

    public ApiResponse<String> addCategory(Category category) {
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Category> ExistingCategory = categoryDao.findByCategoryName(category.getCategoryName());
            if(ExistingCategory.isPresent()){
                throw new Exception("Category Already exist");
            }
            categoryDao.save(category);
            response.setSuccess(true);
            response.setMessage("Category added successfully");
            return response;
        }
        catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
