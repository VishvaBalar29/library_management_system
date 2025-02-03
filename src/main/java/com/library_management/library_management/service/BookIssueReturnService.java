package com.library_management.library_management.service;

import com.library_management.library_management.dao.BookDao;
import com.library_management.library_management.dao.BookIssueReturnDao;
import com.library_management.library_management.dao.UserDao;
import com.library_management.library_management.model.Book;
import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.model.User;
import com.library_management.library_management.projection.BookIssueReturnProjection;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class BookIssueReturnService {

    @Autowired
    BookIssueReturnDao bookIssueReturnDao;

    @Autowired
    BookDao bookDao;

    @Autowired
    UserDao userDao;

    public enum State{
        PENDING,
        REJECT,
        ACCEPTED,
        RETURN
    }


    public ApiResponse<String> issueBook(Integer bookId,Integer userId){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Book> existingBook = bookDao.findById(bookId);
            if(!existingBook.isPresent()){
                throw new Exception("Given Book is not found");
            }
            Optional<User> existingUser = userDao.findById(userId);
            Optional<BookIssueReturn> existingIssueBook = bookIssueReturnDao.findByBookIdAndUserId(bookId,userId);
            if(existingIssueBook.isPresent()){
                throw new Exception("This book is already in pending request");
            }
            BookIssueReturn bookIssueReturn = new BookIssueReturn();
            bookIssueReturn.setBook(existingBook.get());
            bookIssueReturn.setUser(existingUser.get());
            bookIssueReturn.setState(BookIssueReturn.State.PENDING);
            bookIssueReturnDao.save(bookIssueReturn);
            response.setSuccess(true);
            response.setMessage("Book Issued Successfully...");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<List<BookIssueReturn>> getSingleUserRequest(Integer userId,String status) {
        ApiResponse<List<BookIssueReturn>> response = new ApiResponse<>();
        try{
            Optional<User> existingUser = userDao.findById(userId);
            if(status == null || status.isEmpty()){
                response.setMessage("null");
//                Optional<List<BookIssueReturnProjection>> allReq = bookIssueReturnDao.getSingleUserRequest(userId);
                Optional<List<BookIssueReturn>> allReq = bookIssueReturnDao.findByUserId(userId);

                response.setData(allReq.orElseThrow(()->new Exception("No Request Found")));
            }
            else{
                try {
                    State.valueOf(status.toUpperCase());
                } catch (Exception e) {
                    throw new Exception("Invalid status");
                }
//                Optional<List<BookIssueReturnProjection>> allReq = bookIssueReturnDao.getSingleUserFilterRequest(userId,status.toUpperCase());
//                response.setData(allReq.orElseThrow(()->new Exception("No Request Found")));
            }

            response.setSuccess(true);
            response.setMessage("All Request Fetched");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<List<BookIssueReturnProjection>> getAllRequest(Integer userId,String status){
        ApiResponse<List<BookIssueReturnProjection>> response = new ApiResponse<>();
        try{
            Optional<User> user = userDao.findById(userId);
            if(!user.get().getIs_admin()){
                throw new Exception("You're not Admin");
            }
            if(status.equals("")){
                response.setMessage("null");
                Optional<List<BookIssueReturnProjection>> allRequest = bookIssueReturnDao.getAllRequest();
                response.setData(allRequest.orElseThrow(()->new Exception("No Request Found")));
            }
            else{
                Optional<List<BookIssueReturnProjection>> allRequest = bookIssueReturnDao.getFilterRequest(status);
                response.setData(allRequest.orElseThrow(()->new Exception("No Request Found")));
            }
            response.setSuccess(true);
            response.setMessage("All Request Fetched");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<String> approveRequest(Integer userId,Integer reqId){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<User> user = userDao.findById(userId);
            if(!user.get().getIs_admin()){
                throw new Exception("You're not Admin");
            }
            Optional<BookIssueReturn> existingReq = bookIssueReturnDao.findById(reqId);
            if(!existingReq.isPresent()){
                throw new Exception("Given request Id is not exist");
            }
            if(existingReq.get().getState() != BookIssueReturn.State.PENDING){
                throw new Exception("Given request is not in PENDING state...");
            }
            Optional<User> currAdmin = userDao.findById(userId);
            existingReq.get().setAdmin(currAdmin.get());
            existingReq.get().setState(BookIssueReturn.State.ACCEPTED);
            existingReq.get().setApprovalDate(LocalDate.now());
            existingReq.get().setReturnDate(LocalDate.now().plusDays(10));
            bookIssueReturnDao.save(existingReq.get());
            response.setSuccess(true);
            response.setMessage("Request Approved");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }






}
