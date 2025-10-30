package com.critiquehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
    @Column(unique = true, nullable = false)
    private String name;
    
    @Size(min = 2, max = 50, message = "Slug must be between 2 and 50 characters")
    @Column(unique = true, nullable = false)
    private String slug;
    
    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description;
    
    @Column(name = "color_code", length = 7)
    private String colorCode = "#3B82F6"; // Default blue color
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Many-to-Many relationship with Posts
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();
    
    // Helper methods
    public void addPost(Post post) {
        posts.add(post);
        post.getTags().add(this);
    }
    
    public void removePost(Post post) {
        posts.remove(post);
        post.getTags().remove(this);
    }
    
    public int getPostCount() {
        return posts != null ? posts.size() : 0;
    }
    
    // Constructor for easy creation
    public Tag(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
    
    public Tag(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }
}
