package com.meraj.blog.service;

import java.util.List;

import com.meraj.blog.entity.Comment;
import com.meraj.blog.payload.CommentDto;

public interface CommentService {
	public CommentDto addComment(Long postId, Long userId, CommentDto commentDto);	
	public List<CommentDto> getCommentsByPost(Long postId);
	public void deleteComment(Long commentId, Long userId);
}
