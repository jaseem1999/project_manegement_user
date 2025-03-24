package com.pm.user.project_management.service.user.bank;

import com.pm.user.project_management.entity.bank.BankAccount;
import com.pm.user.project_management.entity.user.UserAuth;
import com.pm.user.project_management.repository.bank.BankAccountRepository;
import com.pm.user.project_management.service.user.auth.UserDataAccess;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankDataAccess {
    private final BankAccountRepository bankAccountRepository;
    private final UserDataAccess userDataAccess;

    public BankDataAccess(BankAccountRepository bankAccountRepository, UserDataAccess userDataAccess) {
        this.bankAccountRepository = bankAccountRepository;
        this.userDataAccess = userDataAccess;
    }

    public List<BankAccount> getBankAccountDetails(){
        UserAuth userAuth = userDataAccess.getUserAuth();
        return bankAccountRepository.findByUserAuth(userAuth).orElse(null);
    }
}
