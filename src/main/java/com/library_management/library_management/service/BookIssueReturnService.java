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


    public ApiResponse<String> issueBook(BookIssueReturn bookIssueReturn){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<Book> existingBook = bookDao.findById(bookIssueReturn.getBook().getBookId());
            if(!existingBook.isPresent()){
                throw new Exception("Given Book is not found");
            }
            Optional<User> existingUser = userDao.findById(bookIssueReturn.getUser().getId());
            if(!existingUser.isPresent()){
                throw new Exception("Given User is not found");
            }
            Optional<BookIssueReturn> existingIssueBook = bookIssueReturnDao.findByBookIdAndUserId(bookIssueReturn.getBook().getBookId(), bookIssueReturn.getUser().getId());
            if(existingIssueBook.isPresent()){
                throw new Exception("This book is already in pending request");
            }
            response.setSuccess(true);
            response.setMessage("Book Issued Successfully...");
            bookIssueReturnDao.save(bookIssueReturn);
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    //    {{url}}/user/admin/allRequest?status=PENDING
    public ApiResponse<List<BookIssueReturnProjection>> getAllPendingRequest(String status){
        ApiResponse<List<BookIssueReturnProjection>> response = new ApiResponse<>();
        try{
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


    //    {{url}}/user/admin/approveRequest/{{id}}/7
    public ApiResponse<String> approveRequest(Integer adminId,Integer reqId){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Optional<BookIssueReturn> existingReq = bookIssueReturnDao.findById(reqId);
            if(!existingReq.isPresent()){
                throw new Exception("Given request Id is not exist");
            }
            Optional<User> currAdmin = userDao.findById(adminId);
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


    public ApiResponse<List<BookIssueReturnProjection>> getSingleUserRequest(Integer id) {
        ApiResponse<List<BookIssueReturnProjection>> response = new ApiResponse<>();
        try{
            Optional<User> existingUser = userDao.findById(id);
            if(!existingUser.isPresent()){
                throw new Exception("User doesn't exist");
            }
            Optional<List<BookIssueReturnProjection>> allReq = bookIssueReturnDao.getSingleUserRequest(id);
            response.setSuccess(true);
            response.setMessage("All Request Fetched");
            response.setData(allReq.orElseThrow(()-> new Exception("Occur error while fetching requests")));
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }




}
