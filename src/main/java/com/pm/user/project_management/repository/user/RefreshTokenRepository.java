package com.pm.user.project_management.repository.user;

import com.pm.user.project_management.entity.user.RefreshToken;
import com.pm.user.project_management.entity.user.UserAuth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken ,Long> {

    Optional<RefreshToken> findByUserAuth(UserAuth userAuth);

    Optional<RefreshToken> findByToken(@NotNull @NotBlank String token);
}
