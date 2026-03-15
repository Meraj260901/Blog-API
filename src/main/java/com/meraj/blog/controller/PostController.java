package com.meraj.blog.controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.meraj.blog.entity.Post;
import com.meraj.blog.payload.PostDto;
import com.meraj.blog.payload.PostResponse;
import com.meraj.blog.service.FileStorageService;
import com.meraj.blog.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	@Autowired
	private PostService postService;	
	
	@Autowired
	private FileStorageService fileStorageService;
	
	public PostController(PostService postService, FileStorageService fileStorageService) {
		this.postService = postService;
		this.fileStorageService= fileStorageService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
	@PostMapping("/user/{userId}")
	public ResponseEntity<PostDto> addPostForUser(@Valid @RequestBody PostDto postDto, @PathVariable Long userId){
		PostDto newPost = postService.addPostForUser(postDto, userId);
		return new ResponseEntity<>(newPost, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir
			){
		
		PostResponse response = postService.getAllPosts(page, size, sortBy, sortDir);						
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<PostResponse> searchPosts(
			@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir
			) {
		
		PostResponse response = postService.searchPosts(keyword, page, size, sortBy, sortDir);				    
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
		
	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
		PostDto postDto = postService.getPostById(postId);				
		return new ResponseEntity<>(postDto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto updatedPostDto, @PathVariable Long id){
		PostDto postDto = postService.updatePost(updatedPostDto, id);
		return new ResponseEntity<>(postDto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")	
	public ResponseEntity<Void> deletePost(@PathVariable Long id){
		postService.deletePost(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/image/{postId}")
	public ResponseEntity<String> uploadPostImage(
			@PathVariable Long postId,
			@RequestParam("image") MultipartFile image ) throws IOException {
		
		String fileName = fileStorageService.storeFile(image);
		PostDto postDto = postService.getPostById(postId);
		postDto.setImageName(fileName);
		postService.savePost(postDto);
		
		return ResponseEntity.ok("Image uploaded successfully: "+ fileName);
	}
	
	@GetMapping("/image/{fileName}")
	public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws IOException {
	    Resource resource = fileStorageService.loadFileAsResource(fileName);

	    // Auto-detect the file mimetype
	    String contentType = Files.probeContentType(resource.getFile().toPath());

	    if (contentType == null) {
	        contentType = "application/octet-stream";
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .body(resource);
	}

}

































