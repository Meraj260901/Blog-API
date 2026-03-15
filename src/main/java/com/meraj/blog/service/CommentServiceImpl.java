package com.meraj.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meraj.blog.entity.Comment;
import com.meraj.blog.entity.Post;
import com.meraj.blog.entity.User;
import com.meraj.blog.exception.ResourceNotFoundException;
import com.meraj.blog.exception.UnauthorizedException;
import com.meraj.blog.payload.CommentDto;
import com.meraj.blog.payload.CommentResponse;
import com.meraj.blog.repository.CommentRepository;
import com.meraj.blog.repository.PostRepository;
import com.meraj.blog.repository.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto addComment(Long postId, Long userId, CommentDto commentDto) {
		Post post =  postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id "+ postId));
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ userId));
		
		Comment comment = modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
		
		Comment saved = commentRepository.save(comment);
		
		CommentDto response = modelMapper.map(saved, CommentDto.class);
		response.setAuthorName(user.getName());
		response.setPostId(postId);
		response.setUserId(userId);
		
		return response;
	}

	@Override
	public List<CommentDto> getCommentsByPost(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id "+ postId));
		
		return commentRepository.findById(postId)
				.stream()
				.map(comment -> {
					CommentDto dto = modelMapper.map(comment, CommentDto.class);
					dto.setAuthorName(comment.getUser().getName());
					dto.setPostId(postId);
					return dto;
				})
				.collect(Collectors.toList());
	}
	
	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found with id "+ commentId));
		
		User currentUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
		
		boolean isOwner = comment.getUser().getId().equals(currentUser.getId());
		boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());
		
		if(!isOwner && !isAdmin) {
			throw new UnauthorizedException("You can only delete your own comments!");
		}				
		
		commentRepository.delete(comment);
	}

}
