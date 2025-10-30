package com.critiquehub.repository;

import com.critiquehub.entity.Post;
import com.critiquehub.entity.PostStatus;
import com.critiquehub.entity.User;
import com.critiquehub.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Optional<Post> findBySlug(String slug);
    
    Boolean existsBySlug(String slug);
    
    List<Post> findByStatus(PostStatus status);
    
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
    
    Page<Post> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
    
    List<Post> findByAuthor(User author);
    
    Page<Post> findByAuthor(User author, Pageable pageable);
    
    Page<Post> findByAuthorAndStatus(User author, PostStatus status, Pageable pageable);
    
    List<Post> findByCategory(Category category);
    
    Page<Post> findByCategory(Category category, Pageable pageable);
    
    Page<Post> findByCategoryAndStatus(Category category, PostStatus status, Pageable pageable);
    
    List<Post> findByIsFeaturedTrueAndStatus(PostStatus status);
    
    Page<Post> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchPublishedPosts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name IN :tagNames AND p.status = 'PUBLISHED'")
    Page<Post> findPublishedPostsByTagNames(@Param("tagNames") List<String> tagNames, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY p.viewCount DESC")
    List<Post> findMostPopularPublishedPosts(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND p.createdAt >= :startDate ORDER BY p.createdAt DESC")
    List<Post> findRecentPublishedPosts(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY p.likeCount DESC")
    List<Post> findMostLikedPublishedPosts(Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author = :author AND p.status = :status")
    Long countByAuthorAndStatus(@Param("author") User author, @Param("status") PostStatus status);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.category = :category AND p.status = 'PUBLISHED'")
    Long countPublishedPostsByCategory(@Param("category") Category category);
}
