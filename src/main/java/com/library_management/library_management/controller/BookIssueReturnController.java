package com.library_management.library_management.controller;

import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.projection.BookIssueReturnProjection;
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

    @GetMapping("/admin/allRequest")
    public ResponseEntity<ApiResponse<List<BookIssueReturnProjection>>> getAllPendingRequest(@RequestParam(required = false) String status){
        ApiResponse<List<BookIssueReturnProjection>> response = bookIssueReturnService.getAllPendingRequest(status);
        try{
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/admin/approveRequest/{adminId}/{reqId}")
    public ResponseEntity<ApiResponse<String>> approveRequest(@PathVariable Integer adminId,@PathVariable Integer reqId){
        ApiResponse<String> response = bookIssueReturnService.approveRequest(adminId,reqId);
        try{
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getRequest/{id}")
    public ResponseEntity<ApiResponse<List<BookIssueReturnProjection>>> getSingleUserRequest(@PathVariable Integer id){
        ApiResponse<List<BookIssueReturnProjection>> response = bookIssueReturnService.getSingleUserRequest(id);
        try{
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }






}
