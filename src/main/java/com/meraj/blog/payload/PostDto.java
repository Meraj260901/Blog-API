package com.meraj.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {
	private Long id;
	
	@NotEmpty(message = "Title cannot be empty")
	@Size(min = 5, message = "Title must have at least 5 characters")
	private String title;
	
	 @NotEmpty(message = "Content cannot be empty")
	 private String content;
	 
	 private String authorName; 
	 
	 private Long userId; 
	 
	 private String imageName;
}
