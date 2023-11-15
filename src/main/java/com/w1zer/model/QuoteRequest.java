package com.w1zer.model;

import com.w1zer.validation.ProfileExists;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
public class QuoteRequest {
    @NotBlank(message = "Content can't be blank")
    @Size(max = 300, message = "Content must be shorter than 300 characters")
    private final String content;

    @Size(max = 50, message = "Author must be shorter than 50 characters")
    private final String author;

    @Size(max = 50, message = "Source must be shorter than 50 characters")
    private final String source;

    @Size(max = 300, message = "Description must be shorter than 300 characters")
    private final String description;

    @NotNull(message = "idProfile can't be null")
    @Positive(message = "idProfile must be positive number")
    @ProfileExists(message = "Profile with idProfile not exists")
    private final Long idProfile;
}
