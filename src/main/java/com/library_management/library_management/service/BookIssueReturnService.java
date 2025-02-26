package com.library_management.library_management.service;

import com.library_management.library_management.dao.BookDao;
import com.library_management.library_management.dao.BookIssueReturnDao;
import com.library_management.library_management.dao.UserDao;
import com.library_management.library_management.model.Book;
import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.model.User;
import com.library_management.library_management.projection.BookIssueReturnProjection;
import com.library_management.library_management.response.BookIssueActionRequest;
import com.library_management.library_management.response.BookReturnResponse;
import com.library_management.library_management.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
            if(existingBook.get().getIs_issued()){
                throw new Exception(("Book is already Issued"));
            }
            Optional<User> existingUser = userDao.findById(userId);
//            Optional<BookIssueReturn> existingIssueBook = bookIssueReturnDao.findByBookIdAndUserId(bookId,userId);
//            if(existingIssueBook.isPresent()){
//                throw new Exception("This book is already in pending request");
//            }
            existingBook.get().setIs_issued(true);
            bookDao.save(existingBook.get());
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
            List<BookIssueReturn> allReq = new ArrayList<>();
            if(status == null || status.isEmpty()){
                allReq = bookIssueReturnDao.findByUserId(userId);
            }
            else{
                try {
                    State.valueOf(status.toUpperCase());
                } catch (Exception e) {
                    throw new Exception("Invalid status");
                }
                allReq = bookIssueReturnDao.findByUserIdAndState(userId,BookIssueReturn.State.valueOf(status.toUpperCase()));
            }
            response.setData(allReq);
            response.setSuccess(true);
            response.setMessage("All Request Fetched");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<List<BookIssueReturn>> getAllRequest(Integer userId,String status){
        ApiResponse<List<BookIssueReturn>> response = new ApiResponse<>();
        try{
            status = status.toUpperCase();
            List<BookIssueReturn> allRequest = new ArrayList<>();
            if(status == null || status.isEmpty()){
                allRequest = bookIssueReturnDao.findAll();
            }
            else{
                allRequest = bookIssueReturnDao.findByState(BookIssueReturn.State.valueOf(status));
            }
            response.setData(allRequest);
            response.setSuccess(true);
            response.setMessage("All Request Fetched");
            return response;
        }
        catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage("Invalid Action...");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<String> bookIssueAction(Integer userId, BookIssueActionRequest bookIssueActionRequest){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<User> user = userDao.findById(userId);
            if(!user.get().getIs_admin()){
                throw new Exception("You're not Admin");
            }
            String action = bookIssueActionRequest.getAction().toUpperCase();
            if(!action.equals("ACCEPTED") && !action.equals("REJECT")){
                throw new Exception("Invalid Action");
            }
            Optional<BookIssueReturn> existingReq = bookIssueReturnDao.findById(bookIssueActionRequest.getBookIssueId());
            if(!existingReq.isPresent()){
                throw new Exception("Given request Id is not exist");
            }
            if(existingReq.get().getState() != BookIssueReturn.State.PENDING){
                throw new Exception("Given request is not in PENDING state...");
            }
            Optional<User> currAdmin = userDao.findById(userId);
            existingReq.get().setAdmin(currAdmin.get());
            existingReq.get().setState(BookIssueReturn.State.valueOf(action));
            existingReq.get().setApprovalDate(LocalDate.now());
            existingReq.get().setReturnDate(LocalDate.now().plusDays(10));
            bookIssueReturnDao.save(existingReq.get());
            response.setSuccess(true);
            response.setMessage("Action Performed Succesfully...");
            return response;
        }
        catch(Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ApiResponse<String> returnBook(BookReturnResponse bookReturnResponse) {
        ApiResponse<String> response = new ApiResponse<>();
        try{
            return response;

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }




}
