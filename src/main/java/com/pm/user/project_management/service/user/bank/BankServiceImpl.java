package com.pm.user.project_management.service.user.bank;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.bank.BankAccountAddRequest;
import com.pm.user.project_management.dto.response.bank.BankAddResponse;
import com.pm.user.project_management.entity.bank.BankAccount;
import com.pm.user.project_management.entity.user.UserAuth;
import com.pm.user.project_management.entity.user.UserInfo;
import com.pm.user.project_management.repository.bank.BankAccountRepository;
import com.pm.user.project_management.service.user.auth.UserDataAccess;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {

    private final BankAccountRepository bankAccountRepository;
    private final UserDataAccess userDataAccess;
    private final BankDataAccess bankDataAccess;

    public BankServiceImpl(BankAccountRepository bankAccountRepository, UserDataAccess userDataAccess, BankDataAccess bankDataAccess) {
        this.bankAccountRepository = bankAccountRepository;
        this.userDataAccess = userDataAccess;
        this.bankDataAccess = bankDataAccess;
    }

    @Override
    public ApiResponse<BankAddResponse> addBankAccount(BankAccountAddRequest accountAddRequest) {
        UserAuth userAuth =userDataAccess.getUserAuth();
        UserInfo userInfo = userDataAccess.getUserInfo(userAuth);

        if (userInfo == null){
            return new ApiResponse<>(null,
                    false,
                    "Please add your user information",
                    HttpStatus.NOT_FOUND);
        }

        List<BankAccount> bankAccounts = bankDataAccess.getBankAccountDetails();
        if(bankAccounts != null){
           boolean isPrimary=bankAccounts
                   .stream()
                   .anyMatch(bankAccount -> bankAccount.getIsPrimary() == true);

           //check the user have already primary account
           if (isPrimary && accountAddRequest.getIsPrimary()){
               ApiResponse<BankAddResponse> apiResponse= new ApiResponse<>(null,
                       false,
                       "You already have a primary account.",
                       HttpStatus.NOT_ACCEPTABLE);
               BankAccount primaryAccount = bankAccounts
                       .stream().filter(bankAccount ->
                               bankAccount.getIsPrimary()).collect(Collectors.toList()).get(0);
               Map<String, Object> metadataResponse = metadataForPrimaryAccount(primaryAccount);
               apiResponse.setMetadata(metadataResponse);

               return apiResponse;
           }
        }

        BankAccount bankAccount = BankAccount.builder()
                .bankName(accountAddRequest.getBankName())
                .branchName(accountAddRequest.getBranchName())
                .accountNumber(accountAddRequest.getAccountNumber())
                .ifscCode(accountAddRequest.getIfscCode())
                .accountHolderName(accountAddRequest.getAccountHolderName())
                .accountType(accountAddRequest.getAccountType())
                .panNumber(accountAddRequest.getPanNumber())
                .swiftCode(accountAddRequest.getSwiftCode())
                .upiId(accountAddRequest.getUpiId())
                .isPrimary(accountAddRequest.getIsPrimary())
                .userAuth(userAuth)
                .createdAt(accountAddRequest.getCreatedAt())
                .createdBy(userAuth.getAuthId())
                .build();

        try {
            bankAccount=bankAccountRepository.save(bankAccount);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ApiResponse<>(null,
                    false,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BankAddResponse bankAddResponse=new BankAddResponse(bankAccount.getBankId(),
                bankAccount.getAccountNumber(),
                bankAccount.getBankName(),
                bankAccount.getBranchName(),
                bankAccount.getIfscCode(),
                bankAccount.getPanNumber(),
                bankAccount.getAccountHolderName(),
                bankAccount.getAccountType(),
                bankAccount.getSwiftCode(),
                bankAccount.getUpiId(),
                bankAccount.getIsPrimary(),
                bankAccount.getCreatedAt(),
                bankAccount.getUpdatedAt(),
                bankAccount.getCreatedBy(),
                bankAccount.getUpdatedBy());

        return new ApiResponse<>(bankAddResponse,
                true,
                "Bank account added successful",
                HttpStatus.OK);
    }

    private static Map<String, Object> metadataForPrimaryAccount(BankAccount primaryAccount) {
        BankAddResponse bankAddResponse=new BankAddResponse(primaryAccount.getBankId(),
                primaryAccount.getAccountNumber(),
                primaryAccount.getBankName(),
                primaryAccount.getBranchName(),
                primaryAccount.getIfscCode(),
                primaryAccount.getPanNumber(),
                primaryAccount.getAccountHolderName(),
                primaryAccount.getAccountType(),
                primaryAccount.getSwiftCode(),
                primaryAccount.getUpiId(),
                primaryAccount.getIsPrimary(),
                primaryAccount.getCreatedAt(),
                primaryAccount.getUpdatedAt(),
                primaryAccount.getCreatedBy(),
                primaryAccount.getUpdatedBy());
        Map<String ,Object> metadataResponse = new HashMap<>();

        metadataResponse.put("bankAccount",bankAddResponse);
        return metadataResponse;
    }
}
