package com.library_management.library_management.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGetAllResponse {
    private int id;
    private String username;
    private String email;
}
