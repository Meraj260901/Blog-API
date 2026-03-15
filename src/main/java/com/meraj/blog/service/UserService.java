package com.meraj.blog.service;

import java.util.List;

import com.meraj.blog.entity.User;
import com.meraj.blog.payload.UserDto;

public interface UserService {
	public UserDto createUser(UserDto userDto);
	public UserDto updateUser(UserDto updatedUserDto, Long id);
	public void deleteUser(Long id);
	public UserDto getUserById(Long id);
	public List<UserDto> getAllUsers();
}
