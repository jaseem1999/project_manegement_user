package com.pm.user.project_management.repository.bank;

import com.pm.user.project_management.entity.bank.BankAccount;
import com.pm.user.project_management.entity.user.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    Optional<List<BankAccount>> findByUserAuth(UserAuth userAuth);
}
