package com.pm.user.project_management.controller.bank;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.bank.BankAccountAddRequest;
import com.pm.user.project_management.dto.response.bank.BankAddResponse;
import com.pm.user.project_management.service.user.bank.BankService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/bank")
@Validated
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<BankAddResponse>> addBankAccount(
            @Valid @RequestBody BankAccountAddRequest accountAddRequest){
        ApiResponse<BankAddResponse> response = bankService.addBankAccount(accountAddRequest);
        return new ResponseEntity<>(response,response.getStatus());
    }

    //TODO :: update api (split)
    //TODO :: get own details api
    //TODO :: get all user bank api;
    //TODO :: delete api;
    //TODO :: status change api

}
