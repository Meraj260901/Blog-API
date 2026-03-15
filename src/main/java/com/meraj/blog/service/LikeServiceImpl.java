package com.meraj.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meraj.blog.entity.Post;
import com.meraj.blog.entity.PostLike;
import com.meraj.blog.entity.User;
import com.meraj.blog.exception.ResourceNotFoundException;
import com.meraj.blog.payload.LikeDto;
import com.meraj.blog.repository.PostLikeRepository;
import com.meraj.blog.repository.PostRepository;
import com.meraj.blog.repository.UserRepository;

@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	private PostLikeRepository likeRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void likePost(Long postId, Long userId) {
		if(likeRepository.existsByPostIdAndUserId(postId, userId)) {
			throw new RuntimeException("Post Already Liked");
		}
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id:" + postId));
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found wiht id: " + userId));
		
		PostLike like = new PostLike();
		like.setPost(post);
		like.setUser(user);
		likeRepository.save(like);
	}
	
	@Transactional
	@Override
	public void unlikePost(Long postId, Long userId) {
		if(!likeRepository.existsByPostIdAndUserId(postId, userId)) {
			return;
		}
		likeRepository.deleteByPostIdAndUserId(postId, userId);
	}

	@Override
	public long getLikeCount(Long postId) {
		return likeRepository.countByPostId(postId);		
	}

	@Override
	public List<LikeDto> getUserIdsWhoLikedPost(Long postId) {
		 List<PostLike> likes = likeRepository.findByPostId(postId);
		 
		 return likes.stream()
				 .map(like -> {
					 LikeDto dto = new LikeDto();
					 dto.setId(like.getId());
					 dto.setUserId(like.getUser().getId());
					 dto.setUserName(like.getUser().getName());
					 dto.setCreatedAt(System.currentTimeMillis());
					 return dto;
				 }).collect(Collectors.toList());
	}
	
	
}
