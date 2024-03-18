package com.w1zer.repository;

import com.w1zer.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    @NonNull
    Optional<RefreshToken> findById(@NonNull Long id);

    Optional<RefreshToken> findByToken(String token);
}
