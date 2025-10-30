package com.critiquehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"}),
    @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeType likeType;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Relationships
    @NotNull(message = "Like must belong to a user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    
    // Helper methods
    public boolean isPostLike() {
        return likeType == LikeType.POST && post != null;
    }
    
    public boolean isCommentLike() {
        return likeType == LikeType.COMMENT && comment != null;
    }
    
    // Factory methods for easy creation
    public static Like createPostLike(User user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setLikeType(LikeType.POST);
        return like;
    }
    
    public static Like createCommentLike(User user, Comment comment) {
        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        like.setLikeType(LikeType.COMMENT);
        return like;
    }
    
    // Validation method
    @PrePersist
    @PreUpdate
    private void validateLike() {
        if (likeType == LikeType.POST && post == null) {
            throw new IllegalStateException("Post like must have a valid post");
        }
        if (likeType == LikeType.COMMENT && comment == null) {
            throw new IllegalStateException("Comment like must have a valid comment");
        }
        if (likeType == LikeType.POST && comment != null) {
            throw new IllegalStateException("Post like cannot have a comment");
        }
        if (likeType == LikeType.COMMENT && post != null) {
            throw new IllegalStateException("Comment like cannot have a post");
        }
    }
    
    // Constructor for post likes
    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
        this.likeType = LikeType.POST;
    }
    
    // Constructor for comment likes
    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
        this.likeType = LikeType.COMMENT;
    }
}
