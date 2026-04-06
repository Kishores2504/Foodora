package com.FoodoraProject.FoodoraProject1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodoraProject.FoodoraProject1.Service.FoodService;

@RestController
@RequestMapping("/foods")
public class FoodController {
	
	@Autowired
	FoodService foodservice;
	
	@GetMapping("/allfoods")
	public ResponseEntity<?> allfoods(@RequestHeader("Authorization") String authHeader){
		return foodservice.allfoods(authHeader);
	}
	
}
