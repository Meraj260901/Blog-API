package com.meraj.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meraj.blog.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
	        String titleKeyword, String contentKeyword, Pageable pageable
	);

}
