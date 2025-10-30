package com.critiquehub.repository;

import com.critiquehub.entity.Comment;
import com.critiquehub.entity.Post;
import com.critiquehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPost(Post post);
    
    Page<Comment> findByPost(Post post, Pageable pageable);
    
    List<Comment> findByPostAndParentIsNull(Post post);
    
    Page<Comment> findByPostAndParentIsNullOrderByCreatedAtDesc(Post post, Pageable pageable);
    
    List<Comment> findByParent(Comment parent);
    
    Page<Comment> findByParentOrderByCreatedAtAsc(Comment parent, Pageable pageable);
    
    List<Comment> findByAuthor(User author);
    
    Page<Comment> findByAuthor(User author, Pageable pageable);
    
    List<Comment> findByIsApprovedTrue();
    
    Page<Comment> findByIsApprovedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    List<Comment> findByIsApprovedFalse();
    
    Page<Comment> findByIsApprovedFalseOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.parent IS NULL AND c.isApproved = true ORDER BY c.createdAt DESC")
    List<Comment> findApprovedTopLevelCommentsByPost(@Param("post") Post post);
    
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.parent IS NULL AND c.isApproved = true ORDER BY c.createdAt DESC")
    Page<Comment> findApprovedTopLevelCommentsByPost(@Param("post") Post post, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.parent = :parent AND c.isApproved = true ORDER BY c.createdAt ASC")
    List<Comment> findApprovedRepliesByParent(@Param("parent") Comment parent);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post AND c.isApproved = true")
    Long countApprovedCommentsByPost(@Param("post") Post post);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author = :author AND c.isApproved = true")
    Long countApprovedCommentsByAuthor(@Param("author") User author);
    
    @Query("SELECT c FROM Comment c WHERE c.createdAt >= :startDate AND c.isApproved = true ORDER BY c.createdAt DESC")
    List<Comment> findRecentApprovedComments(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.isApproved = true ORDER BY c.likeCount DESC")
    List<Comment> findMostLikedApprovedComments(Pageable pageable);
}
