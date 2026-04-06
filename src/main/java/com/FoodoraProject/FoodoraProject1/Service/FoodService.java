package com.FoodoraProject.FoodoraProject1.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.FoodoraProject.FoodoraProject1.DTOs.FoodDTO;
import com.FoodoraProject.FoodoraProject1.Entity.FoodEntity;
import com.FoodoraProject.FoodoraProject1.Entity.UserEntity;
import com.FoodoraProject.FoodoraProject1.Repository.FoodRepository;
import com.FoodoraProject.FoodoraProject1.Repository.UserRepository;
import com.FoodoraProject.FoodoraProject1.Security.JwtUtility;

@Service
public class FoodService {
	
	@Autowired
	FoodRepository foodrepo;
	
	@Autowired
	JwtUtility jwtutil;
	
	@Autowired
	UserRepository userrepo;
	
	public ResponseEntity<?> allfoods(String authHeader) {
		// TODO Auto-generated method stub
		String tokenn=authHeader.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		Optional<UserEntity> isuser=userrepo.findByemailid(useremail);
		

		if(isuser.isEmpty() || jwtutil.isExpired(jwtutil.getClaims(tokenn).getExpiration())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorizied.");
		}
		
		List<FoodEntity>isfoodlist=foodrepo.findAll();
		if(isfoodlist.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No food items.");
		}
		List<FoodDTO>foodsdto=isfoodlist.stream()
								.map(f-> {
										String imagedata=null;
										if(f.getImagedata()!=null) {
											imagedata=Base64.getEncoder().encodeToString(f.getImagedata());
										}
										FoodDTO dto=new FoodDTO(f.getFoodid() ,f.getFoodname(), f.getDescription(),
										f.getPrice(), f.getImagename(), f.getImagetype(),imagedata,f.getCategory().getCategoryname());
										return dto;
										}).toList();
		return ResponseEntity.status(HttpStatus.OK).body(foodsdto);
	}
	
	
}
