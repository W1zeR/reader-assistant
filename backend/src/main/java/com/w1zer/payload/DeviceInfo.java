package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public record DeviceInfo(
        @NotBlank(message = BROWSER_NAME_NOT_NULL_MESSAGE)
        String browserName,

        @NotNull(message = DEVICE_TYPE_NOT_NULL_MESSAGE)
        String deviceType
) {
    private static final String BROWSER_NAME_NOT_NULL_MESSAGE = "Browser name can't be null";
    private static final String DEVICE_TYPE_NOT_NULL_MESSAGE = "Device type can't be null";
}
