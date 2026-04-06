package com.FoodoraProject.FoodoraProject1.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.FoodoraProject.FoodoraProject1.DTOs.CategoryDTO;
import com.FoodoraProject.FoodoraProject1.Entity.Category;
import com.FoodoraProject.FoodoraProject1.Entity.Role;
import com.FoodoraProject.FoodoraProject1.Entity.UserEntity;
import com.FoodoraProject.FoodoraProject1.Repository.CategoryRepository;
import com.FoodoraProject.FoodoraProject1.Repository.UserRepository;
import com.FoodoraProject.FoodoraProject1.Security.JwtUtility;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository catrepo;
	
	@Autowired
	JwtUtility jwtutil;
	
	@Autowired
	UserRepository userrepo;

	public ResponseEntity<?> allcategory(String token) {
		// TODO Auto-generated method stub
		if(token==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed");
		}
		String tokenn=token.substring(7);
		String username=jwtutil.extractUsername(tokenn);
		Optional<UserEntity>isuser=userrepo.findByemailid(username);
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not exist.");
		}
		
		List<Category>category=catrepo.findAll();
		if(category.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Category Available");
		}
		List<CategoryDTO> categories=category.stream()
									.map(c -> {
										String imagedata=null;
									if(c.getImagedata()!=null) {
										imagedata=Base64.getEncoder().encodeToString(c.getImagedata());
									}
									return new CategoryDTO(c.getCategoryid() ,c.getCategoryname(), c.getImagename(), c.getImagetype(),imagedata); 
									})
									.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(categories);
	}
	
	
	
}
