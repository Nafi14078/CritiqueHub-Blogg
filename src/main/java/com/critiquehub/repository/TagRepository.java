package com.critiquehub.repository;

import com.critiquehub.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    Optional<Tag> findBySlug(String slug);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);

    boolean existsByNameAndIdNot(@NotBlank @Size(min = 1, max = 50) String name, Long id);

    boolean existsBySlugAndIdNot(String newSlug, Long id);
}