package com.critiquehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class PostRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Long categoryId;

    private Set<Long> tagIds;

    // Getters and setters
}