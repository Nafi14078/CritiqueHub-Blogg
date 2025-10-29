package com.critiquehub.service;

import com.critiquehub.dto.request.PostRequestDTO;
import com.critiquehub.dto.response.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostResponseDTO createPost(PostRequestDTO postRequest);
    PostResponseDTO getPostById(Long id);
    PostResponseDTO updatePost(Long id, PostRequestDTO postRequest);
    void deletePost(Long id);
    Page<PostResponseDTO> getAllPosts(Pageable pageable);
}