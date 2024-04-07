package com.w1zer.payload;

import com.w1zer.validation.NotBlankIfPresent;
import com.w1zer.validation.UniqueTagName;
import jakarta.validation.constraints.Size;

import static com.w1zer.constants.EntityConstants.TAG_NAME_LENGTH;

public record TagRequest(
        @UniqueTagName
        @NotBlankIfPresent
        @Size(max = TAG_NAME_LENGTH)
        String name
) {
}
