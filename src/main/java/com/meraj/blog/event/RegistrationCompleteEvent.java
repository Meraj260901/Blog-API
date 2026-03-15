package com.meraj.blog.event;

import org.springframework.context.ApplicationEvent;

import com.meraj.blog.entity.User;

import lombok.Getter;

@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {
	private final User user;
	private final String applicationUrl;
	
	public RegistrationCompleteEvent(User user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}
}
