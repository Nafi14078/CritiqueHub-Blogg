package com.critiquehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Post title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false)
    private String title;
    
    @Size(min = 5, max = 250, message = "Slug must be between 5 and 250 characters")
    @Column(unique = true, nullable = false)
    private String slug;
    
    @Size(max = 300, message = "Excerpt must not exceed 300 characters")
    @Column(columnDefinition = "TEXT")
    private String excerpt;
    
    @NotBlank(message = "Post content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(name = "featured_image_url")
    private String featuredImageUrl;
    
    @Column(name = "reading_time")
    private Integer readingTime; // in minutes
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;
    
    @Column(name = "comment_count", nullable = false)
    private Long commentCount = 0L;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT;
    
    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;
    
    @Column(name = "allow_comments", nullable = false)
    private Boolean allowComments = true;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @NotNull(message = "Post must belong to a user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();
    
    // Helper methods
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }
    
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }
    
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
        this.commentCount = (long) comments.size();
    }
    
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
        this.commentCount = (long) comments.size();
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void updateLikeCount() {
        this.likeCount = (long) likes.size();
    }
    
    public boolean isPublished() {
        return status == PostStatus.PUBLISHED;
    }
    
    public boolean isDraft() {
        return status == PostStatus.DRAFT;
    }
    
    // Constructor for easy creation
    public Post(String title, String slug, String content, User author) {
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.author = author;
    }
    
    public Post(String title, String slug, String excerpt, String content, User author, Category category) {
        this.title = title;
        this.slug = slug;
        this.excerpt = excerpt;
        this.content = content;
        this.author = author;
        this.category = category;
    }
}

enum PostStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}
