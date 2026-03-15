package com.meraj.blog.service;

import java.util.List;

import com.meraj.blog.payload.LikeDto;

public interface LikeService {
	void likePost(Long postId, Long userId);
	void unlikePost(Long postId, Long userId);
	long getLikeCount(Long postId);
	List<LikeDto> getUserIdsWhoLikedPost(Long postId);
}
