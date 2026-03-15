package com.meraj.blog.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
	private Long id;
	private Long userId;
	private String userName;
	private Long createdAt;
}
