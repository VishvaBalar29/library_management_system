package com.library_management.library_management.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookIssueActionRequest {
    private int bookIssueId;
    private String action;
}
