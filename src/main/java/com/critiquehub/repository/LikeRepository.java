package com.critiquehub.repository;

import com.critiquehub.entity.Like;
import com.critiquehub.entity.LikeType;
import com.critiquehub.entity.Post;
import com.critiquehub.entity.Comment;
import com.critiquehub.entity.User;
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
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByUserAndPost(User user, Post post);
    
    Optional<Like> findByUserAndComment(User user, Comment comment);
    
    Boolean existsByUserAndPost(User user, Post post);
    
    Boolean existsByUserAndComment(User user, Comment comment);
    
    List<Like> findByUser(User user);
    
    Page<Like> findByUser(User user, Pageable pageable);
    
    List<Like> findByPost(Post post);
    
    Page<Like> findByPost(Post post, Pageable pageable);
    
    List<Like> findByComment(Comment comment);
    
    Page<Like> findByComment(Comment comment, Pageable pageable);
    
    List<Like> findByLikeType(LikeType likeType);
    
    Page<Like> findByLikeTypeOrderByCreatedAtDesc(LikeType likeType, Pageable pageable);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
    Long countLikesByPost(@Param("post") Post post);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.comment = :comment")
    Long countLikesByComment(@Param("comment") Comment comment);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.user = :user AND l.likeType = :likeType")
    Long countLikesByUserAndType(@Param("user") User user, @Param("likeType") LikeType likeType);
    
    @Query("SELECT l FROM Like l WHERE l.createdAt >= :startDate ORDER BY l.createdAt DESC")
    List<Like> findRecentLikes(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT l.post, COUNT(l) as likeCount FROM Like l WHERE l.likeType = 'POST' AND l.createdAt >= :startDate GROUP BY l.post ORDER BY likeCount DESC")
    List<Object[]> findMostLikedPostsSince(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT l.comment, COUNT(l) as likeCount FROM Like l WHERE l.likeType = 'COMMENT' AND l.createdAt >= :startDate GROUP BY l.comment ORDER BY likeCount DESC")
    List<Object[]> findMostLikedCommentsSince(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    void deleteByUserAndPost(User user, Post post);
    
    void deleteByUserAndComment(User user, Comment comment);
}
