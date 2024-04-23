package com.w1zer.payload;

public record BookAuthorRequest(
        String surname,

        String name,

        String patronymic
) {
}
