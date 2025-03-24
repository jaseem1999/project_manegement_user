package com.pm.user.project_management.repository.user;

import com.pm.user.project_management.entity.user.UserAuth;
import com.pm.user.project_management.entity.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    boolean existsByEmployeeId(String employeeId);

    boolean existsByPersonalEmail(String personalEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserInfo> findByUserAuth(UserAuth userAuth);
}
