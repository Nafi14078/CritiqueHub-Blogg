package com.critiquehub.service;

import com.critiquehub.dto.request.TagCreateRequest;
import com.critiquehub.dto.request.TagUpdateRequest;
import com.critiquehub.dto.response.ApiResponse;
import com.critiquehub.dto.response.TagResponse;
import com.critiquehub.entity.Tag;
import com.critiquehub.exception.ResourceNotFoundException;
import com.critiquehub.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public ApiResponse<TagResponse> createTag(TagCreateRequest request) {
        // Check if tag already exists
        if (tagRepository.existsByName(request.getName())) {
            return new ApiResponse<>(false, "Tag with this name already exists", null);
        }

        String slug = generateSlug(request.getName());

        if (tagRepository.existsBySlug(slug)) {
            return new ApiResponse<>(false, "Tag with this slug already exists", null);
        }

        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSlug(slug);

        Tag savedTag = tagRepository.save(tag);
        TagResponse response = convertToResponse(savedTag);

        return new ApiResponse<>(true, "Tag created successfully", response);
    }

    public ApiResponse<TagResponse> updateTag(Long id, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        // Check if new name already exists (excluding current tag)
        if (tagRepository.existsByNameAndIdNot(request.getName(), id)) {
            return new ApiResponse<>(false, "Tag with this name already exists", null);
        }

        String newSlug = generateSlug(request.getName());
        if (tagRepository.existsBySlugAndIdNot(newSlug, id)) {
            return new ApiResponse<>(false, "Tag with this slug already exists", null);
        }

        tag.setName(request.getName());
        tag.setSlug(newSlug);

        Tag updatedTag = tagRepository.save(tag);
        TagResponse response = convertToResponse(updatedTag);

        return new ApiResponse<>(true, "Tag updated successfully", response);
    }

    public ApiResponse<TagResponse> getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        TagResponse response = convertToResponse(tag);
        return new ApiResponse<>(true, "Tag retrieved successfully", response);
    }

    public ApiResponse<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponse> responses = tags.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Tags retrieved successfully", responses);
    }

    public ApiResponse<String> deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        tagRepository.delete(tag);
        return new ApiResponse<>(true, "Tag deleted successfully", null);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    private TagResponse convertToResponse(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getSlug(),
                tag.getCreatedAt(),
                tag.getUpdatedAt()
        );
    }
}