package com.library_management.library_management.controller;

import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.projection.BookIssueReturnProjection;
import com.library_management.library_management.response.BookIssueActionRequest;
import com.library_management.library_management.response.BookReturnResponse;
import com.library_management.library_management.service.BookIssueReturnService;
import com.library_management.library_management.utility.ApiResponse;
import com.library_management.library_management.utility.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-issue")
public class BookIssueReturnController {

    @Autowired
    BookIssueReturnService bookIssueReturnService;

    @GetMapping("/issueBook/{bookId}")
    public ResponseEntity<ApiResponse<String>> issueBook(HttpServletRequest request , @PathVariable Integer bookId){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserInfo userinfo = (UserInfo) request.getAttribute("userData");
            if(userinfo.is_admin()){
                throw new Exception("You can't issue book ....because you're admin");
            }
            response = bookIssueReturnService.issueBook(bookId,userinfo.getUserId());
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getRequest")
    public ResponseEntity<ApiResponse<List<BookIssueReturn>>> getSingleUserRequest(HttpServletRequest request,@RequestParam(required=false) String status){
        ApiResponse<List<BookIssueReturn>> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo)request.getAttribute("userData");
            response = bookIssueReturnService.getSingleUserRequest(userInfo.getUserId(),status);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/allRequest")
    public ResponseEntity<ApiResponse<List<BookIssueReturn>>> getAllPendingRequest(HttpServletRequest request,@RequestParam(required = false) String status){
        ApiResponse<List<BookIssueReturn>> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo) request.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = bookIssueReturnService.getAllRequest(userInfo.getUserId(),status);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/issue-action")
    public ResponseEntity<ApiResponse<String>> bookIssueAction(HttpServletRequest httpServletRequest, @RequestBody BookIssueActionRequest bookIssueActionRequest)   {
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo) httpServletRequest.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = bookIssueReturnService.bookIssueAction(userInfo.getUserId(), bookIssueActionRequest);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return-book")
    public ResponseEntity<ApiResponse<String>> returnBook(HttpServletRequest httpServletRequest, @RequestBody BookReturnResponse bookReturnResponse){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserInfo userInfo = (UserInfo)httpServletRequest.getAttribute("userData");
            if(!userInfo.is_admin()){
                throw new Exception("You're not an Admin");
            }
            response = bookIssueReturnService.returnBook(bookReturnResponse);
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }







}
