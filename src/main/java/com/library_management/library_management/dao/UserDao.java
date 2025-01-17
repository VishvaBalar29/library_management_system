package com.library_management.library_management.dao;

import com.library_management.library_management.model.User;
import com.library_management.library_management.response.UserGetAllResponse;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    User findByEmailAndPassword(String email,String password);

    @Query(value = "SELECT id,username,email FROM users",nativeQuery = true)
    List<UserGetAllResponse> getAllUsers();

}
