package com.critiquehub.repository;

import com.critiquehub.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findByName(String name);
    
    Optional<Tag> findBySlug(String slug);
    
    Boolean existsByName(String name);
    
    Boolean existsBySlug(String slug);
    
    List<Tag> findByIsActiveTrue();
    
    List<Tag> findByIsActiveTrueOrderByNameAsc();
    
    List<Tag> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    
    Set<Tag> findByNameInIgnoreCaseAndIsActiveTrue(Set<String> tagNames);
    
    @Query("SELECT t FROM Tag t WHERE t.isActive = true AND SIZE(t.posts) > 0 ORDER BY t.name")
    List<Tag> findActiveTagsWithPosts();
    
    @Query("SELECT t FROM Tag t WHERE t.isActive = true ORDER BY SIZE(t.posts) DESC")
    List<Tag> findActiveTagsOrderByPostCountDesc();
    
    @Query("SELECT t FROM Tag t WHERE t.isActive = true ORDER BY SIZE(t.posts) DESC LIMIT :limit")
    List<Tag> findTopTagsByPostCount(@Param("limit") int limit);
    
    @Query("SELECT COUNT(p) FROM Tag t JOIN t.posts p WHERE t.id = :tagId AND p.status = 'PUBLISHED'")
    Long countPublishedPostsByTagId(@Param("tagId") Long tagId);
}
