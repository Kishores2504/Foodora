package com.FoodoraProject.FoodoraProject1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodoraProject.FoodoraProject1.Service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	CategoryService catservice;
	
	@GetMapping("/allcategory")
	public ResponseEntity<?>allcategory(@RequestHeader("Authorization")String token){
		return catservice.allcategory(token);
	}
	
}
