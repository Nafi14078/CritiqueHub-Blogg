package com.critiquehub.service.impl;

import com.critiquehub.dto.request.PostRequestDTO;
import com.critiquehub.dto.response.PostResponseDTO;
import com.critiquehub.entity.Category;
import com.critiquehub.entity.Post;
import com.critiquehub.entity.Tag;
import com.critiquehub.exception.ResourceNotFoundException;
import com.critiquehub.repository.CategoryRepository;
import com.critiquehub.repository.PostRepository;
import com.critiquehub.repository.TagRepository;
import com.critiquehub.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequest) {
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Set<Tag> tags = (postRequest.getTagIds() != null) ?
                postRequest.getTagIds().stream()
                        .map(id -> tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + id)))
                        .collect(Collectors.toSet())
                : Set.of();

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(category);
        post.setTags(tags);

        Post saved = postRepository.save(post);
        return mapToDTO(saved);
    }

    @Override
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return mapToDTO(post);
    }

    @Override
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Set<Tag> tags = (postRequest.getTagIds() != null) ?
                postRequest.getTagIds().stream()
                        .map(tagId -> tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId)))
                        .collect(Collectors.toSet())
                : Set.of();

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(category);
        post.setTags(tags);

        Post updated = postRepository.save(post);
        return mapToDTO(updated);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    @Override
    public Page<PostResponseDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::mapToDTO);
    }

    private PostResponseDTO mapToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setCategoryId(post.getCategory().getId());
        dto.setTagIds(post.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        return dto;
    }
}