package com.meraj.blog.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Comment text cannot be blank")
	private String content;
	
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
	@JsonIgnore
    private Post post;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;	
}




