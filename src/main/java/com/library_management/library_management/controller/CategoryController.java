package com.library_management.library_management.controller;

import com.library_management.library_management.model.Category;
import com.library_management.library_management.service.CategoryService;
import com.library_management.library_management.utility.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/admin/addCategory")
    public ResponseEntity<ApiResponse<String>> addCategory(@Valid @RequestBody Category category, BindingResult result){
        ApiResponse<String> response = categoryService.addCategory(category);
        try{
            if(result.hasErrors()){
                throw new Exception(result.getAllErrors().get(0).getDefaultMessage());
            }
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
