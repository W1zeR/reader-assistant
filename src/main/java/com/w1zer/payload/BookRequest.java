package com.w1zer.payload;

import jakarta.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public record BookRequest(
        @NotBlank
        String title
) {
}
