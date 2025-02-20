package com.library_management.library_management.service;

import com.library_management.library_management.dao.BookDao;
import com.library_management.library_management.model.Book;
import com.library_management.library_management.projection.BookProjection;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.tree.ExpandVetoException;
import java.util.List;
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

    public ApiResponse<List<Book>> getAllBooks(){
        ApiResponse<List<Book>> response = new ApiResponse<>();
        try{
//            List<BookProjection> allBooks = bookDao.getBooks();
            List<Book> allBooks = bookDao.findAll();
            response.setSuccess(true);
            response.setMessage("List Of All Books");
            response.setData(allBooks);
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<String> deleteBook(Integer id){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Book> existingBook = bookDao.findById(id);
            if(!existingBook.isPresent()){
                throw new Exception("Given Id is not Exist");
            }
            bookDao.deleteById(id);
            response.setSuccess(true);
            response.setMessage("Book deleted successfully");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<String> updateBook(Book book, Integer id){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Book> existingBook = bookDao.findById(id);
            if(!existingBook.isPresent()){
                throw new Exception("Given Id is not Exist");
            }
            if(book.getBookName().equals("")){
                throw new Exception("Please enter valid Book name");
            }
            existingBook.get().setBookName(book.getBookName());
            existingBook.get().setCategory(book.getCategory());
            bookDao.save(existingBook.get());
            response.setSuccess(true);
            response.setMessage("Book Updated Successfully");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }


}
