package com.meraj.blog.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	public String storeFile(MultipartFile file) throws IOException{
		Path dirPath = Paths.get(uploadDir);
		if(!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path filePath = dirPath.resolve(fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		return fileName;
	}
	
	public Resource loadFileAsResource(String fileName) throws MalformedURLException{
		Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
		return new UrlResource(filePath.toUri());
	}
	
}


