package com.critiquehub.controller;

import com.critiquehub.dto.request.PostRequestDTO;
import com.critiquehub.dto.response.PostResponseDTO;
import com.critiquehub.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public PostResponseDTO createPost(@Valid @RequestBody PostRequestDTO request) {
        return postService.createPost(request);
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public PostResponseDTO updatePost(@PathVariable Long id, @Valid @RequestBody PostRequestDTO request) {
        return postService.updatePost(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @GetMapping
    public Page<PostResponseDTO> listPosts(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }
}