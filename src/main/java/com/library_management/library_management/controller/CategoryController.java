package com.library_management.library_management.controller;

import com.library_management.library_management.model.Category;
import com.library_management.library_management.projection.CategoryProjection;
import com.library_management.library_management.service.CategoryService;
import com.library_management.library_management.utility.ApiResponse;
import com.library_management.library_management.utility.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/addCategory")
    public ResponseEntity<ApiResponse<String>> addCategory(@Valid @RequestBody Category category, BindingResult result,HttpServletRequest request){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo)request.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = categoryService.addCategory(category);
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

    @GetMapping("/getAllCategory")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategory(HttpServletRequest request){
        ApiResponse<List<Category>> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo)request.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = categoryService.getAllCategory();
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

    @PatchMapping("/updateCategory/{id}")
    public ResponseEntity<ApiResponse<String>> updateCategory(HttpServletRequest request,@RequestBody Category category,@PathVariable int id){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo)request.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = categoryService.updateCategory(category,id);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(HttpServletRequest request,@PathVariable int id){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo)request.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = categoryService.deleteCategory(id);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

}
