package com.pm.user.project_management.repository.user;

import com.pm.user.project_management.entity.user.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    UserAuth findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByOfficialNumber(String officialNumber);
}
