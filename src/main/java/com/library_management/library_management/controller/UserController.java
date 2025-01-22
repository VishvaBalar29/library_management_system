package com.library_management.library_management.controller;

import com.library_management.library_management.model.UserLogin;
import com.library_management.library_management.response.UserGetAllResponse;
import com.library_management.library_management.service.UserService;
import com.library_management.library_management.utility.ApiResponse;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.library_management.library_management.model.User;
import com.library_management.library_management.utility.UserInfo;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userservice;

    @GetMapping("/hello")
    public String helloWorld(){
        return "Hello World";
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signUp(@Valid @RequestBody User user, BindingResult result){
        ApiResponse<String> response = new ApiResponse<>();
        if(result.hasErrors()){
            response.setSuccess(false);
            response.setMessage(result.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        response = userservice.signUp(user);
        if(!response.isSuccess()){
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<HashMap<String,String>>> login(@RequestBody User user){
        ApiResponse<HashMap<String,String>> response = new ApiResponse<>();
        try {
            response = userservice.login(user);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            response.setMessage("User Succesfully Logged In");
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("admin/all-users")
    public ResponseEntity<ApiResponse<List<UserGetAllResponse>>> getAllUsers(HttpServletRequest request){
            ApiResponse<List<UserGetAllResponse>> response = new ApiResponse<>();
            try {
                UserInfo userInfo = (UserInfo) request.getAttribute("userData");
                if(!userInfo.is_admin()){
                    throw new Exception("You are not authorized!!");
                }
                response = userservice.getAllUsers();
                return new ResponseEntity<>(response,HttpStatus.OK);
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMessage(e.getMessage());
                return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
            }
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(){
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("You're logout");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



}
