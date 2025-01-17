package com.library_management.library_management.model;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "usrname can't be null")
    private String username;

    @NotNull(message = "email can'e be null")
    @Email(message = "please enter valid email id")
    private String email;

    @Size(min = 3,message = "Password should be at least 3 characters long")
    private String password;

    private Boolean is_admin;
}
