package com.library_management.library_management.dao;

import com.library_management.library_management.model.Book;
import com.library_management.library_management.model.BookIssueReturn;
import com.library_management.library_management.projection.BookIssueReturnProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookIssueReturnDao extends JpaRepository<BookIssueReturn, Integer> {

    List<BookIssueReturn> findByState(BookIssueReturn.State state);

    Optional<BookIssueReturn> findByBook(Book book);

//    Optional<BookIssueReturn> findByBookIdAndUserId(Integer bookId,Integer userId);

    List<BookIssueReturn> findByUserId(Integer userId);

    List<BookIssueReturn> findByUserIdAndState(Integer userId,BookIssueReturn.State state);


}
