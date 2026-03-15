package com.meraj.blog.payload;

import java.util.List;
import com.meraj.blog.entity.Post;

import lombok.Data;

@Data
public class PostResponse {
	private List<PostDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
