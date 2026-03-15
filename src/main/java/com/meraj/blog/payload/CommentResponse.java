package com.meraj.blog.payload;

import java.util.List;

import lombok.Data;

@Data
public class CommentResponse {
	private List<CommentDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
