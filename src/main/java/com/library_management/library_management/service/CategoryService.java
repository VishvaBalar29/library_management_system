package com.library_management.library_management.service;

import com.library_management.library_management.dao.BookDao;
import com.library_management.library_management.dao.CategoryDao;
import com.library_management.library_management.model.Book;
import com.library_management.library_management.model.Category;
import com.library_management.library_management.projection.CategoryProjection;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BookDao bookDao;

    public ApiResponse<String> addCategory(Category category) {
        ApiResponse<String> response = new ApiResponse<>();
        try{
            if(category.getCategoryName().equals("")){
                throw new Exception("Please Enter Category Name");
            }
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

    public ApiResponse<String> updateCategory(Category category,int id){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Category> existingCategory = categoryDao.findById(id);
            if(!existingCategory.isPresent()){
                throw new Exception("Given Id (category) is not exist");
            }
            existingCategory.get().setCategoryName(category.getCategoryName());
            categoryDao.save(existingCategory.get());
            response.setSuccess(true);
            response.setMessage("Category Updated Successfully");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<String> deleteCategory(int id) {
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Category> existingCategory = categoryDao.findById(id);
            if(!existingCategory.isPresent()){
                throw new Exception("Given Id (category) is not exist");
            }
            Optional<List<Book>> existingBooks = bookDao.findByCategory_catId(id);
            if(existingBooks.get().size() > 0){
                throw new Exception("Books are exist related this category, so first remove those Books");
            }
            categoryDao.deleteById(id);
            response.setSuccess(true);
            response.setMessage("Category Deleted Successfully");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<List<CategoryProjection>> getAllCategory() {
        ApiResponse<List<CategoryProjection>> response = new ApiResponse<>();
        try{
            Optional<List<CategoryProjection>> categories = categoryDao.getAllCategory();
            response.setSuccess(true);
            response.setMessage("All Category successfully fetched");
            response.setData(categories.orElseThrow(()->new Exception("No categories found")));
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
