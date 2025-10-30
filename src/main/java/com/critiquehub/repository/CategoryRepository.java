package com.critiquehub.repository;

import com.critiquehub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    Optional<Category> findBySlug(String slug);
    
    Boolean existsByName(String name);
    
    Boolean existsBySlug(String slug);
    
    List<Category> findByIsActiveTrue();
    
    List<Category> findByIsActiveTrueOrderByNameAsc();
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true AND SIZE(c.posts) > 0 ORDER BY c.name")
    List<Category> findActiveCategoriesWithPosts();
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY SIZE(c.posts) DESC")
    List<Category> findActiveCategoriesOrderByPostCountDesc();
    
    @Query("SELECT COUNT(p) FROM Category c JOIN c.posts p WHERE c.id = :categoryId AND p.status = 'PUBLISHED'")
    Long countPublishedPostsByCategoryId(@Param("categoryId") Long categoryId);
}
