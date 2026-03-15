package com.meraj.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meraj.blog.payload.LikeDto;
import com.meraj.blog.security.SecurityUtils;
import com.meraj.blog.service.LikeService;

@RestController
@RequestMapping("/api/like")
public class PostLikeController {
	
	@Autowired
	private LikeService likeService;
	
	@PostMapping("/{postId}")
	public ResponseEntity<String> likePost(@PathVariable Long postId){
		Long userId = SecurityUtils.getCurrentUserId();
		likeService.likePost(postId, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body("Post Liked successfully");
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<String> unlikePost(@PathVariable Long postId){
		Long userId = SecurityUtils.getCurrentUserId();
		likeService.unlikePost(postId, userId);
		return ResponseEntity.ok("Post unliked successfully");
	}
	
	@GetMapping("/count/{postId}")
	public ResponseEntity<Long> getLikeCount(@PathVariable Long postId){
		Long count = likeService.getLikeCount(postId);
		return ResponseEntity.ok(count);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<List<LikeDto>> getUserIdWhoLikedPost(@PathVariable Long postId){
		List<LikeDto> likeDto = likeService.getUserIdsWhoLikedPost(postId);
		return new ResponseEntity<>(likeDto, HttpStatus.OK);
	}
}
