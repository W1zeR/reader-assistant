package com.w1zer.repository;

import com.w1zer.entity.RefreshToken;
import com.w1zer.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    @Override
    @NonNull
    Optional<UserDevice> findById(@NonNull Long id);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByProfileId(Long profileId);
}