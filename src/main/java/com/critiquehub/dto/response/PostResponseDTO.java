package com.critiquehub.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    private Set<Long> tagIds;

    // Getters and setters
}