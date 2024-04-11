package com.w1zer.payload;

public record BookAuthorResponse(
        Long id,

        String surname,

        String name,

        String patronymic
) {
}
