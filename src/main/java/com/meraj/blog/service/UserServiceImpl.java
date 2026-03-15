package com.meraj.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meraj.blog.entity.User;
import com.meraj.blog.payload.UserDto;
import com.meraj.blog.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		User saveduser = userRepository.save(user);
		return modelMapper.map(saveduser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto updatedUserDto, Long id) {
		User exisitingUser = userRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		exisitingUser.setEmail(updatedUserDto.getEmail());
		exisitingUser.setName(updatedUserDto.getName());
		User updatedUser = userRepository.save(exisitingUser);
		return modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public void deleteUser(Long id) {
		User exisitingUser = userRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		userRepository.delete(exisitingUser);
	}
	
	//Post id belongs to which user
	@Override
	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return modelMapper.map(user, UserDto.class);
	}
	
	@Override
	public List<UserDto> getAllUsers(){
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(user -> modelMapper.map(users, UserDto.class))
				.collect(Collectors.toList());
	}
	
}
