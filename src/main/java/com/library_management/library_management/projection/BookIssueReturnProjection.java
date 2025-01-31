package com.library_management.library_management.projection;

import java.time.LocalDate;

public interface BookIssueReturnProjection {
    Integer getId();
    Integer getBookId();
    Integer getUserId();
    Integer getAdminId();
    LocalDate getIssueDate();
    LocalDate getApprovalDate();
    LocalDate getReturnDate();
    LocalDate getUserReturnDate();
    String getState();
}
