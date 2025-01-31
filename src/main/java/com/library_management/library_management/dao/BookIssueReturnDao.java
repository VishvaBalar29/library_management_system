package com.library_management.library_management.dao;

import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.projection.BookIssueReturnProjection;
import com.library_management.library_management.projection.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookIssueReturnDao extends JpaRepository<BookIssueReturn, Integer> {

    @Query(value = "select * from book_issue_return where book_id = :book_id and user_id = :id limit 1",nativeQuery = true)
    Optional<BookIssueReturn> findByBookIdAndUserId(Integer book_id, Integer id);


    @Query(value = "SELECT id, book_id, user_id, admin_id, issue_date, approval_date, return_date, user_return_date, state from book_issue_return",nativeQuery = true)
    Optional<List<BookIssueReturnProjection>> getAllRequest();


    @Query(value = "SELECT id, book_id, user_id, admin_id, issue_date, approval_date, return_date, user_return_date, state from book_issue_return where state = :Status",nativeQuery = true)
    Optional<List<BookIssueReturnProjection>> getFilterRequest(String Status);

}
