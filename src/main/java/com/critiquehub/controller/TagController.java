package com.critiquehub.controller;

import com.critiquehub.dto.request.TagCreateRequest;
import com.critiquehub.dto.request.TagUpdateRequest;
import com.critiquehub.dto.response.ApiResponse;
import com.critiquehub.dto.response.TagResponse;
import com.critiquehub.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Tag management APIs")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Create a new tag")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@Valid @RequestBody TagCreateRequest request) {
        ApiResponse<TagResponse> response = tagService.createTag(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing tag")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(@PathVariable Long id,
                                                              @Valid @RequestBody TagUpdateRequest request) {
        ApiResponse<TagResponse> response = tagService.updateTag(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get tag by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> getTagById(@PathVariable Long id) {
        ApiResponse<TagResponse> response = tagService.getTagById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all tags")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        ApiResponse<List<TagResponse>> response = tagService.getAllTags();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a tag")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTag(@PathVariable Long id) {
        ApiResponse<String> response = tagService.deleteTag(id);
        return ResponseEntity.ok(response);
    }
}
