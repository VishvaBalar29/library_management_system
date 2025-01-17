package com.library_management.library_management.service;

import com.library_management.library_management.model.UserLogin;
import com.library_management.library_management.response.UserGetAllResponse;
import com.library_management.library_management.utility.ApiResponse;
import com.library_management.library_management.utility.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.library_management.library_management.dao.UserDao;
import com.library_management.library_management.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

        @Autowired
        UserDao userdao;

        @Autowired
        JwtUtil jwtUtil;

        public ApiResponse<String> signUp(User user) {
                ApiResponse response = new ApiResponse<>();
                try {
                        Optional<User> existingUser = userdao.findByEmail(user.getEmail());
                    if (existingUser.isPresent()) {
                        throw new Exception("Email is already registered");
                    }
                        if(user.getIs_admin() == null){
                            user.setIs_admin(false);
                        }
                        userdao.save(user);
                        response.setSuccess(true);
                        response.setMessage("User Created Successfully");
                        return response;
                } catch (Exception e) {
                        response.setSuccess(false);
                        response.setMessage(e.getMessage());
                        return response;
                }
        }

        public ApiResponse<HashMap<String,String>> login(User user) {
                ApiResponse<HashMap<String,String>> response = new ApiResponse<>();
                try {
                       Optional<User> existingUser = userdao.findByEmail(user.getEmail());
                       if(existingUser.isEmpty()){
                                throw new Exception("User Not Found");
                       }  
                       if(!existingUser.get().getPassword().equals(user.getPassword())){
                                throw new Exception("Invalid Credentials");
                       }
                       boolean is_admin = existingUser.get().getIs_admin() == null ? false : existingUser.get().getIs_admin();
                       String token = jwtUtil.generateToken(existingUser.get().getUsername(),is_admin);
                       System.out.println("Token : " + token);
                       System.out.println("\n");
                       System.out.println("\n");
                       System.out.println("\n");
                       System.out.println("\n");
                       System.out.println("\n");
                       HashMap<String,String> body = new HashMap<>();
                       response.setSuccess(true);
                       body.put("username", existingUser.get().getUsername());
                       body.put("token",token);
                       response.setData(body);
                       return response;
                } catch (Exception e) {
                        response.setSuccess(false);
                        response.setMessage(e.getMessage());
                        return response;
                }

        }

        public ApiResponse<List<UserGetAllResponse>> getAllUsers() {
                ApiResponse<List<UserGetAllResponse>> response = new ApiResponse<>();
                try {
                        List<UserGetAllResponse> users = userdao.getAllUsers();
                        response.setData(users);
                        response.setSuccess(true);
                } catch (Exception e) {
                        response.setMessage(e.getMessage());
                        response.setSuccess(false);
                }
                return response;
        }
}
