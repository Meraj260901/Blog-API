package com.meraj.blog.service;

import com.meraj.blog.payload.PostDto;
import com.meraj.blog.payload.PostResponse;

public interface PostService {
	PostDto addPostForUser(PostDto postDto, Long userId);
	PostDto getPostById(Long postId);
	PostDto updatePost(PostDto updatedPostDto, Long postId);
	void deletePost(Long postId);
	PostResponse getAllPosts(int page, int size, String sortBy, String sortDir);
	PostResponse searchPosts(String keyword, int page, int size, String sortBy, String sortDir);
	PostDto savePost(PostDto postDto);
}
