package com.critiquehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagCreateRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}