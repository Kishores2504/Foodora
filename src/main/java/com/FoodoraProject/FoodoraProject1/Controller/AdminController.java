package com.FoodoraProject.FoodoraProject1.Controller;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FoodoraProject.FoodoraProject1.DTOs.CategoryDTO;
import com.FoodoraProject.FoodoraProject1.Service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminservice;
	
	@PostMapping("/addcategory")
	public ResponseEntity<?>addcategory(@RequestParam String catname, 
										@RequestParam MultipartFile image ,
										@RequestHeader("Authorization")String token)
												throws IOException{
		return adminservice.addcategory(catname,image,token);
	}
	
	@GetMapping("/allcategory")
	public ResponseEntity<?>allcategory(@RequestHeader("Authorization") String token){
		return adminservice.allcategories(token);
	}
	
	@DeleteMapping("/deletecategory")
	public ResponseEntity<?>deletecategory(@RequestParam String categoryname,@RequestHeader("Authorization")String token){
		return adminservice.deletecategory(categoryname,token);
	}
	
	@GetMapping("/allusers")
	public ResponseEntity<?>allusers(@RequestHeader("Authorization")String token){
		return adminservice.allusers(token);
	}
	
	@PostMapping("/addfood")
	public ResponseEntity<?>addfood(@RequestParam String foodname,
									@RequestParam String description,
									@RequestParam double price,
									@RequestParam(required = false) int quantity,
									@RequestParam String categoryname,
									@RequestParam MultipartFile image,
									@RequestHeader("Authorization")String token
									) throws IOException{
		return adminservice.addfood(foodname,description,price,quantity,categoryname,image,token);
	}
	@PutMapping("/editfood")
	public ResponseEntity<?>editfood(@RequestParam int id,
									@RequestParam String foodname,
									@RequestParam String description,
									@RequestParam double price,
									@RequestParam String categoryname,
									@RequestParam(required = false) MultipartFile image,
									@RequestHeader("Authorization")String token
									) throws IOException{
		return adminservice.editfood(id,foodname,description,price,categoryname,image,token);
	}
	
	@GetMapping("/allfoods")
	public ResponseEntity<?> allfoods(@RequestHeader("Authorization") String token){
		return adminservice.allfoods(token);
	}
	
	@DeleteMapping("/deletefood")
	public ResponseEntity<?> deletefood(@RequestParam int foodid,@RequestHeader("Authorization")String token){
		return adminservice.deletefood(foodid,token);
	}
	
	@GetMapping("/allorders")
	public ResponseEntity<?> allorders(@RequestHeader("Authorization")String token){
		return adminservice.allorders(token);
	}
	
}
