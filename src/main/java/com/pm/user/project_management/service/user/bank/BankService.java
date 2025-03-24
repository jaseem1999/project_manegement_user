package com.pm.user.project_management.service.user.bank;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.bank.BankAccountAddRequest;
import com.pm.user.project_management.dto.response.bank.BankAddResponse;
import jakarta.validation.Valid;

public interface BankService {
    ApiResponse<BankAddResponse> addBankAccount(@Valid BankAccountAddRequest accountAddRequest);
}
