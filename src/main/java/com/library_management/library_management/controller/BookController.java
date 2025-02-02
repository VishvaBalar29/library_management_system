package com.library_management.library_management.controller;

import com.library_management.library_management.model.Book;
import com.library_management.library_management.projection.BookProjection;
import com.library_management.library_management.service.BookService;
import com.library_management.library_management.utility.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class BookController {

    @Autowired

    BookService bookService;

    @PostMapping("/admin/addBook")
    public ResponseEntity<ApiResponse<String>>  addBook(@Valid @RequestBody Book book, BindingResult result){
        ApiResponse response = bookService.addBook(book);
        try{
            if(result.hasErrors()){
                throw new Exception(result.getAllErrors().get(0).getDefaultMessage());
            }
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allBooks")
    public ResponseEntity<ApiResponse<List<BookProjection>>> getAllBooks(){
        ApiResponse<List<BookProjection>> response = bookService.getAllBooks();
        try{
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("admin/deleteBook/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Integer id){
        ApiResponse<String> response = bookService.deleteBook(id);
        try{
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("admin/updateBook/{id}")
    public ResponseEntity<ApiResponse<String>> updateBook(@PathVariable Integer id,@Valid @RequestBody Book book,BindingResult result){
        ApiResponse<String> response = bookService.updateBook(book,id);
        try{
            if(result.hasErrors()){
                response.setSuccess(false);
                response.setMessage(result.getAllErrors().get(0).getDefaultMessage());
                throw new Exception(response.getMessage());
            }
            if(!response.isSuccess()){
                throw new Exception(response.getMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
