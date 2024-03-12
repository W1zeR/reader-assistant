package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeviceInfo {
    private static final String DEVICE_ID_NOT_BLANK_MESSAGE = "Device id can't be blank";
    private static final String DEVICE_TYPE_NOT_NULL_MESSAGE = "Device type can't be null";

    @NotBlank(message = DEVICE_ID_NOT_BLANK_MESSAGE)
    private String deviceId;

    @NotNull(message = DEVICE_TYPE_NOT_NULL_MESSAGE)
    private String deviceType;
}
