package com.w1zer.service;

import com.w1zer.entity.RefreshToken;
import com.w1zer.entity.UserDevice;
import com.w1zer.exception.TokenRefreshException;
import com.w1zer.payload.DeviceInfo;
import com.w1zer.repository.UserDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDeviceService {
    private final UserDeviceRepository userDeviceRepository;

    public UserDeviceService(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    public Optional<UserDevice> findByProfileId(Long userId) {
        return userDeviceRepository.findByProfileId(userId);
    }

    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceRepository.findByRefreshToken(refreshToken);
    }

    public UserDevice createUserDevice(DeviceInfo deviceInfo) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceInfo.getDeviceId());
        userDevice.setDeviceType(deviceInfo.getDeviceType());
        userDevice.setIsRefreshActive(true);
        return userDevice;
    }

    public void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken).orElseThrow(
                () -> new TokenRefreshException(refreshToken.getToken(),
                        "No device found for the matching token. Please login again")
        );
        if (!userDevice.getIsRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(),
                    "Refresh blocked for this device. Please login through different device");
        }
    }
}
