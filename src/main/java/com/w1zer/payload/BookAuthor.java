package com.w1zer.payload;

public record BookAuthor(
        Long id,

        String surname,

        String name,

        String patronymic
) {
}
