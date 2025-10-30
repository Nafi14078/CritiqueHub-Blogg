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
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;
    
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = true;
    
    @Column(name = "is_edited", nullable = false)
    private Boolean isEdited = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @NotNull(message = "Comment must belong to a user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @NotNull(message = "Comment must belong to a post")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    // Self-referencing relationship for replies
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Comment> replies = new HashSet<>();
    
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();
    
    // Helper methods
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParent(this);
    }
    
    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParent(null);
    }
    
    public boolean isReply() {
        return parent != null;
    }
    
    public boolean hasReplies() {
        return replies != null && !replies.isEmpty();
    }
    
    public int getReplyCount() {
        return replies != null ? replies.size() : 0;
    }
    
    public void updateLikeCount() {
        this.likeCount = (long) likes.size();
    }
    
    public void markAsEdited() {
        this.isEdited = true;
    }
    
    public void approve() {
        this.isApproved = true;
    }
    
    public void disapprove() {
        this.isApproved = false;
    }
    
    // Constructor for easy creation
    public Comment(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }
    
    public Comment(String content, User author, Post post, Comment parent) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.parent = parent;
    }
}
