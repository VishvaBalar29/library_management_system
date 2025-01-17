package com.library_management.library_management.service;

import com.library_management.library_management.dao.BookDao;
import com.library_management.library_management.model.Book;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.tree.ExpandVetoException;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookDao bookDao;

    public ApiResponse<String> addBook(Book book) {
        ApiResponse response = new ApiResponse();
        try {
            Optional<Book> existingBook =  bookDao.findBybookName(book.getBookName());
            if(existingBook.isPresent()){
                throw new Exception("Book is already exist");
            }
            bookDao.save(book);
            response.setSuccess(true);
            response.setMessage("Book added successfully");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
