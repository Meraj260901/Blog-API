package com.meraj.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.meraj.blog.payload.CommentDto;
import com.meraj.blog.security.SecurityUtils;
import com.meraj.blog.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private final CommentService commentService;	
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}		
	
	@PostMapping("/{postId}")
	public ResponseEntity<CommentDto> addComment(@PathVariable Long postId, @Valid @RequestBody CommentDto commentDto) {
		
		Long userId = SecurityUtils.getCurrentUserId();
		CommentDto saved = commentService.addComment(postId, userId, commentDto);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId){
		List<CommentDto> comments = commentService.getCommentsByPost(postId);		
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){				
		Long userId = SecurityUtils.getCurrentUserId();				
		commentService.deleteComment(commentId, userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
}
