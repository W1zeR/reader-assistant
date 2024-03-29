package com.w1zer.payload;

public record QuoteRequest(
//        @NotBlank(message = "Content can't be blank")
//        @Size(max = 300, message = "Content must be shorter than 300 characters")
//        String content,
//
//        @Size(max = 50, message = "Author must be shorter than 50 characters")
//        String author,
//
//        @Size(max = 50, message = "Source must be shorter than 50 characters")
//        String source,
//
//        @Size(max = 300, message = "Description must be shorter than 300 characters")
//        String description,
//
//        @NotNull(message = "idProfile can't be null")
//        @Positive(message = "idProfile must be positive number")
//        @ProfileExistsById(message = "Profile with idProfile not exists")
//        Long idProfile
        Long id
) {
}
