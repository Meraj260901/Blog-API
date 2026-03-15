package com.meraj.blog.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.meraj.blog.entity.Post;
import com.meraj.blog.entity.User;
import com.meraj.blog.exception.ResourceNotFoundException;
import com.meraj.blog.exception.UnauthorizedException;
import com.meraj.blog.payload.PostDto;
import com.meraj.blog.payload.PostResponse;
import com.meraj.blog.repository.PostRepository;
import com.meraj.blog.repository.UserRepository;
import com.meraj.blog.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public PostDto addPostForUser(PostDto postDto, Long userId) {
		Post post = modelMapper.map(postDto, Post.class);
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Entity not found"));
		 
		post.setUser(user);
		Post savedPost = postRepository.save(post);
		 
		PostDto responseDto = modelMapper.map(savedPost, PostDto.class);
		responseDto.setAuthorName(savedPost.getUser().getName());
		responseDto.setUserId(savedPost.getUser().getId());
		
		return responseDto;
	}
	
	@Override
	public PostDto getPostById(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
		
		PostDto postDto = modelMapper.map(post, PostDto.class);
		postDto.setAuthorName(post.getUser().getName());
		postDto.setUserId(post.getUser().getId());
		
		return postDto;
		
	}
	
	@Override
	public PostDto updatePost(PostDto updatedPostDto, Long postId) {
		Post existingPost = postRepository.findById(postId)
				  .orElseThrow(() -> new ResourceNotFoundException("Post Entity not found"));
				
		String email = SecurityUtils.getCurrentUserEmail();
		User currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email : " +  email));
		
		boolean isOwner = existingPost.getUser().getId().equals(currentUser.getId());
		boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());
		
		if(!isOwner && !isAdmin) {
			throw new UnauthorizedException("You can only modify your own posts!");
		}			
		
		existingPost.setTitle(updatedPostDto.getTitle());
		existingPost.setContent(updatedPostDto.getContent());
		
		Post updatedPost = postRepository.save(existingPost);
		
		PostDto responseDto = modelMapper.map(updatedPost, PostDto.class);
		responseDto.setAuthorName(updatedPost.getUser().getName());
		responseDto.setUserId(updatedPost.getUser().getId());
		
		return responseDto;
	}

	@Override
	public void deletePost(Long postId) {
		Post existingPost = postRepository.findById(postId)
				  .orElseThrow(() -> new ResourceNotFoundException("Post Entity not found"));
				
		String email = SecurityUtils.getCurrentUserEmail();
		User currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email : " +  email));
		
		boolean isOwner = existingPost.getUser().getId().equals(currentUser.getId());
		boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());
		
		if(!isOwner && !isAdmin) {
			throw new UnauthorizedException("You can only delete your own posts!");
		}
				
		postRepository.deleteById(postId);
	}

	@Override
	public PostResponse getAllPosts(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ?
				Sort.by(sortBy).ascending() :
	            Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<Post> posts = postRepository.findAll(pageable);
		
		List<PostDto> content = posts.getContent()
				.stream()
				.map(post -> {
					PostDto dto = modelMapper.map(post, PostDto.class);
					dto.setAuthorName(post.getUser().getName());
					dto.setUserId(post.getUser().getId());
					return dto;
				})
				.toList();
		
		PostResponse responseDto = new PostResponse();
		responseDto.setContent(content);
		responseDto.setPageNumber(posts.getNumber());
	    responseDto.setPageSize(posts.getSize());
	    responseDto.setTotalElements(posts.getTotalElements());
	    responseDto.setTotalPages(posts.getTotalPages());
	    responseDto.setLast(posts.isLast());
	    
	    return responseDto;
	}

	@Override
	public PostResponse searchPosts(String keyword, int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
        Page<Post> result = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                keyword, keyword, pageable
        );
        
        List<PostDto> content = result.getContent()
				.stream()
				.map(post -> {
					PostDto dto = modelMapper.map(post, PostDto.class);
					dto.setAuthorName(post.getUser().getName());
					dto.setUserId(post.getUser().getId());
					return dto;
				})
				.toList();
		
		PostResponse responseDto = new PostResponse();
		responseDto.setContent(content);
		responseDto.setPageNumber(result.getNumber());
	    responseDto.setPageSize(result.getSize());
	    responseDto.setTotalElements(result.getTotalElements());
	    responseDto.setTotalPages(result.getTotalPages());
	    responseDto.setLast(result.isLast());
	    
	    return responseDto;
	}

	@Override
	public PostDto savePost(PostDto postDto) {
		Post post = modelMapper.map(postDto, Post.class);
		Post savedPost = postRepository.save(post);
		return modelMapper.map(savedPost, PostDto.class);
	}
	
	

}
