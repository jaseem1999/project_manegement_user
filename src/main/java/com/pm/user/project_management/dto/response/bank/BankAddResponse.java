package com.pm.user.project_management.dto.response.bank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAddResponse {
    private Long bankId;
    private String accountNumber;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private String panNumber;
    private String accountHolderName;
    private String accountType;
    private String swiftCode;
    private String upiId;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
