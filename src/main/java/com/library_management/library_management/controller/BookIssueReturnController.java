package com.library_management.library_management.controller;

import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.projection.BookIssueReturnProjection;
import com.library_management.library_management.service.BookIssueReturnService;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class BookIssueReturnController {

    @Autowired
    BookIssueReturnService bookIssueReturnService;

    @PostMapping("/issueBook")
    public ResponseEntity<ApiResponse<String>> issueBook(@RequestBody BookIssueReturn bookIssueReturn){
        ApiResponse<String> response = bookIssueReturnService.issueBook(bookIssueReturn);
        try{
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
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
