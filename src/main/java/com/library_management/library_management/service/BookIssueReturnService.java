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
import java.time.Period;
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

            Optional<BookIssueReturn> exisingIssueReq = bookIssueReturnDao.findByBook(existingBook.get());
            if(exisingIssueReq.isPresent() &&  !exisingIssueReq.get().getState().equals(BookIssueReturn.State.REJECT) && !exisingIssueReq.get().getState().equals(BookIssueReturn.State.RETURN)){
                throw new Exception("This book is already requested by other user");
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

            // set true or false in issued status in books table
            Optional<Book> existingBook = bookDao.findById(existingReq.get().getBook().getBookId());
            if(action.equals("ACCEPTED") || action.equals("accepted")){
                existingBook.get().setIs_issued(true);
            }
            if(action.equals("REJECT") || action.equals("reject")){
                existingBook.get().setIs_issued(false);
            }
            bookDao.save(existingBook.get());
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
            Optional<BookIssueReturn> existingIssuedBook = bookIssueReturnDao.findById(bookReturnResponse.getBookIssueId());
            if(!existingIssuedBook.isPresent()){
                throw new Exception("Given Book is not issued");
            }

            //check book is issued or not
            if (!existingIssuedBook.get().getState().equals(BookIssueReturn.State.ACCEPTED)) {
                throw new Exception("Book is not in issued state");
            }

            // check return date
            LocalDate currDate = LocalDate.now();
            LocalDate returnDate = existingIssuedBook.get().getReturnDate();
            if(currDate.isAfter(returnDate)){
                Period period = Period.between(returnDate, currDate);
                int charge = period.getDays() * 100;
                existingIssuedBook.get().setCharge(charge);
            }
            else{
                existingIssuedBook.get().setCharge(0);
            }
            existingIssuedBook.get().setUserReturnDate(currDate);
            existingIssuedBook.get().setState(BookIssueReturn.State.RETURN);
            bookIssueReturnDao.save(existingIssuedBook.get());

            // set book issued status false
            Optional<Book> existingBook = bookDao.findById(existingIssuedBook.get().getBook().getBookId());
            existingBook.get().setIs_issued(false);
            bookDao.save(existingBook.get());

            response.setSuccess(true);
            response.setMessage("Book Returned Successfully...");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

}
