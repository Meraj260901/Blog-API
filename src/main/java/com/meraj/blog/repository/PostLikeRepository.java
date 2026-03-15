package com.meraj.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meraj.blog.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	boolean existsByPostIdAndUserId(Long postId, Long userId);
	void deleteByPostIdAndUserId(Long postId, Long userId);
	long countByPostId(Long postId);
	List<PostLike> findByPostId(Long postId);
}
