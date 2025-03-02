package com.library_management.library_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private User admin;

    @Column(name = "issueDate", nullable = false)
    private LocalDate issueDate;

    @Column(name = "approvalDate")
    private LocalDate approvalDate;

    @Column(name = "returnDate")
    private LocalDate returnDate;

    @Column(name = "userReturnDate")
    private LocalDate userReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    public enum State{
        PENDING,
        REJECT,
        ACCEPTED,
        RETURN
    }

    @Column(name="charge")
    private Integer charge;

    @PrePersist
    public void setIssueDateBeforePersist() {
        if (issueDate == null) {
            issueDate = LocalDate.now();  // Sets today's date if issueDate is not set
        }
    }


}
