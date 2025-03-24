package com.pm.user.project_management.dto.request.bank;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountAddRequest {

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Branch name is required")
    private String branchName;

    @NotBlank(message = "Account number is required")
    @Size(min = 8, max = 20, message = "Account number must be between 8 and 20 characters")
    private String accountNumber;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    private String ifscCode;

    @NotBlank(message = "Account holder name is required")
    @Size(max = 50, message = "Account holder name must be at most 50 characters")
    private String accountHolderName;

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(SAVINGS|CURRENT)$", message = "Account type must be SAVINGS or CURRENT")
    private String accountType;

    @NotBlank(message = "PAN number required")
    private String panNumber;

    @Pattern(regexp = "^[A-Z0-9]{8,11}$", message = "Invalid SWIFT code format")
    private String swiftCode;

    @Pattern(regexp = "^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$", message = "Invalid UPI ID format")
    private String upiId;

    @NotNull(message = "Primary status is required")
    private Boolean isPrimary;

    @PastOrPresent(message = "Created at must be in the past or present")
    private LocalDateTime createdAt;

}

