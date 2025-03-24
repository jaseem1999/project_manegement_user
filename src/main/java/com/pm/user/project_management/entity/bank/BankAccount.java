package com.pm.user.project_management.entity.bank;


import com.pm.user.project_management.entity.user.UserAuth;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long bankId;

    @Column(length = 100, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String branchName;

    @Column(nullable = false)
    private String ifscCode;

    @Column(nullable = false)
    private String panNumber;

    @Column(nullable = false)
    private String accountHolderName;
    @Column(nullable = false)
    private String accountType;

    private String swiftCode;

    private String upiId;

    @Column(nullable = false)
    private Boolean isPrimary;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    private UserAuth userAuth;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long createdBy;

    private Long updatedBy;
}
