package com.FoodoraProject.FoodoraProject1.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.FoodoraProject.FoodoraProject1.DTOs.CategoryDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.FinalOrderDto;
import com.FoodoraProject.FoodoraProject1.DTOs.FoodDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.FoodFinalDetails;
import com.FoodoraProject.FoodoraProject1.DTOs.UserDTO;
import com.FoodoraProject.FoodoraProject1.Entity.Category;
import com.FoodoraProject.FoodoraProject1.Entity.FinalOrder;
import com.FoodoraProject.FoodoraProject1.Entity.FoodEntity;
import com.FoodoraProject.FoodoraProject1.Entity.OrderItems;
import com.FoodoraProject.FoodoraProject1.Entity.Role;
import com.FoodoraProject.FoodoraProject1.Entity.Status;
import com.FoodoraProject.FoodoraProject1.Entity.UserEntity;
import com.FoodoraProject.FoodoraProject1.Repository.CategoryRepository;
import com.FoodoraProject.FoodoraProject1.Repository.FinalOrderRepository;
import com.FoodoraProject.FoodoraProject1.Repository.FoodRepository;
import com.FoodoraProject.FoodoraProject1.Repository.OrderItemRepository;
import com.FoodoraProject.FoodoraProject1.Repository.UserRepository;
import com.FoodoraProject.FoodoraProject1.Security.JwtUtility;

@Service
public class AdminService {

	@Autowired
	CategoryRepository catrepo;

	@Autowired
	JwtUtility jwtutil;

	@Autowired
	UserRepository userrepo;

	@Autowired
	FoodRepository foodrepo;

	@Autowired
	FinalOrderRepository orderrepo;
	
	@Autowired
	OrderItemRepository orderitemrepo;

	public boolean istoken(String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return false;
		}
		return true;
	}

	public ResponseEntity<?> addcategory(String catname, MultipartFile image, String token) throws IOException {
		// TODO Auto-generated method stub
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid.");
		}
		if (image.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add image.");
		}
		String catnamee = catname.toLowerCase();
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);

		Optional<UserEntity> isuser = userrepo.findByemailid(useremail);

		if (isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		UserEntity user = isuser.get();

		if (!user.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only Admin can add.");
		}

		Optional<Category> iscategory = catrepo.findBycategoryname(catnamee);
		if (iscategory.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body("Category already exist.");
		}
		Category category = new Category();
		category.setCategoryname(catnamee);
		category.setImagename(image.getOriginalFilename());
		category.setImagetype(image.getContentType());
		category.setImagedata(image.getBytes());
		catrepo.save(category);

		return ResponseEntity.status(HttpStatus.OK).body("Category added succesfully");
	}

	public ResponseEntity<?> deletecategory(String categoryname, String token) {
		// TODO Auto-generated method stub
		String catnamee = categoryname.toLowerCase();
		String tokenn = null;
		if (token != null && token.startsWith("Bearer "))
			tokenn = token.substring(7);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");

		String useremail = jwtutil.extractUsername(tokenn);
		Optional<UserEntity> isuser = userrepo.findByemailid(useremail);
		if (isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not exist.");
		}
		UserEntity user = isuser.get();
		if (user.getRole() != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cant delete the category.");
		}
		Optional<Category> iscategory = catrepo.findBycategoryname(catnamee);
		if (iscategory.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no category.");
		}
		Category category = iscategory.get();
		List<FoodEntity>foods = category.getFoods();
		for(FoodEntity f : foods) {
			orderitemrepo.deleteByfoodid(f.getFoodid());
		}
		foodrepo.deleteByCategoryid(category.getCategoryid());
		catrepo.Deletebycategoryname(category.getCategoryname());
		return ResponseEntity.status(HttpStatus.OK).body("Category Deleted.");
	}

	public ResponseEntity<?> allusers(String token) {
		// TODO Auto-generated method stub
		String tokenn = null;
		if (istoken(token))
			tokenn = token.substring(7);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");

		String useremail = jwtutil.extractUsername(tokenn);
		Optional<UserEntity> isuser = userrepo.findByemailid(useremail);
		if (isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
		}
		UserEntity user = isuser.get();

		if (user.getRole() == Role.USER) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed.");
		}

		List<UserEntity> users = userrepo.findAll();

		if (users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There are no users.");
		}
		
		List<UserDTO> userdtos = users.stream().filter(u -> u.getRole() == Role.USER)
				.map(u -> {
					List<FinalOrder> orderlist = orderrepo.findByUserid(u.getUserid());
					int orders = orderlist.size();
					return new UserDTO(u.getUserid(), u.getUsername(), u.getEmailid(),orders);
					}).toList();
		
		return ResponseEntity.status(HttpStatus.OK).body(userdtos);
	}

	public ResponseEntity<?> addfood(String foodname, String description, double price, int quantity,
			String categoryname, MultipartFile image, String token) throws IOException {
		// TODO Auto-generated method stub
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid.");
		}
		String catnamee = categoryname.toLowerCase();
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		Optional<UserEntity> isuser = userrepo.findByemailid(useremail);
		if (isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isuser);
		}
		UserEntity user = isuser.get();

		if (user.getRole() == Role.USER) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed.");
		}
		Optional<FoodEntity> isfood = foodrepo.findByfoodname(foodname);

		if (isfood.isPresent()) {
			FoodEntity dfood = isfood.get();
			if (dfood.getPrice() == price) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Food already exist.");
			}
		}
		Optional<Category> iscategory = catrepo.findBycategoryname(catnamee);

		if (iscategory.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no such category.");
		}
		Category category = iscategory.get();
		FoodEntity food = new FoodEntity();
		food.setFoodname(foodname);
		food.setDescription(description);
		food.setPrice(price);
		food.setQuantity(quantity);
		food.setCategory(category);
		if (image != null) {
			food.setImagename(image.getOriginalFilename());
			food.setImagetype(image.getContentType());
			food.setImagedata(image.getBytes());
		}
		foodrepo.save(food);
		return ResponseEntity.status(HttpStatus.OK).body("Food saved.");
	}

	public ResponseEntity<?> editfood(int id, String foodname, String description, double price,
			String categoryname, MultipartFile image, String token) throws IOException {
		// TODO Auto-generated method stub
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid.");
		}
		String tokenn = token.substring(7);
		String category = categoryname.toLowerCase();
		String useremail = jwtutil.extractUsername(tokenn);

		if (userrepo.findByemailid(useremail).isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found.");
		}
		Optional<FoodEntity> isfood = foodrepo.findById(id);
		if (isfood.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Food not found.");
		}
		Optional<Category> iscat = catrepo.findBycategoryname(category);
		if (iscat.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no category.");
		}
		Category cat = iscat.get();
		FoodEntity food = isfood.get();
		food.setFoodname(foodname != null ? foodname : food.getFoodname());
		food.setDescription(description != null ? description : food.getDescription());
		food.setPrice(price != 0 ? price : food.getPrice());
		food.setCategory(cat != null ? cat : food.getCategory());
		food.setImagename(image != null ? image.getOriginalFilename() : food.getImagename());
		food.setImagetype(image != null ? image.getContentType() : food.getImagetype());
		food.setImagedata(image != null ? image.getBytes() : food.getImagedata());
		foodrepo.save(food);

		return ResponseEntity.status(HttpStatus.OK).body("Update Successfull.");
	}
	
	public ResponseEntity<?> allfoods(String token){
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		UserEntity user = userrepo.findByemailid(useremail).orElseThrow(()-> new UsernameNotFoundException("User not found"));
		
		if(user.getRole() != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed");
		}
		
		List<FoodEntity> foods = foodrepo.findAll();
		
		if(foods.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body("Add Food To See");
		}
		
		List<FoodDTO> fooddto = foods.stream().map(f ->{
			String imagedata = null;
			String imagetype = null;
			String imagename = null;
			if(f.getImagedata() != null) {
				 imagedata = Base64.getEncoder().encodeToString(f.getImagedata());
				 imagetype = f.getImagetype();
				 imagename = f.getImagename();
			}
			FoodDTO fd =new FoodDTO(f.getFoodid(), f.getFoodname(), f.getDescription(), f.getPrice(), imagename, imagetype, imagedata, f.getCategory().getCategoryname());
			return fd;
		}).toList();
		
		return ResponseEntity.status(HttpStatus.OK).body(fooddto);
	}

	public ResponseEntity<?> deletefood(int foodid, String token) {
		// TODO Auto-generated method stub
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid.");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		Optional<UserEntity> isuser = userrepo.findByemailid(useremail);
		if (isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not exist.");
		}
		UserEntity user = isuser.get();
		if (user.getRole() != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cant delete the category.");
		}
		List<OrderItems> order = orderitemrepo.existbyfoodid(foodid);
		if(!order.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body("Food Not Deleted. A order item exist.");
		}
		orderitemrepo.deleteByfoodid(foodid);
		foodrepo.deleteByfoodid(foodid);
		return ResponseEntity.status(HttpStatus.OK).body("Food deleted successfully.");
	}

	public ResponseEntity<?> allorders(String token) {
		// TODO Auto-generated method stub
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Token");
		} 
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		UserEntity user = userrepo.findByemailid(useremail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		if (user.getRole() != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed");
		}
		List<FinalOrder> orders = orderrepo.findAll();

		if (orders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Orders.");
		}
		List<FinalOrderDto> finalorders = orders.stream().map(order -> {

			List<FoodFinalDetails> fd = order.getItems().stream()
					.map(item -> new FoodFinalDetails(
							item.getFood().getFoodid(), 
							item.getFood().getQuantity(),
							item.getFood().getPrice()))
					.toList();

			return new FinalOrderDto(order.getTotalamount(), fd,order.getOrderDateTime());
		}).toList();

		return ResponseEntity.status(HttpStatus.OK).body(finalorders);
	}

	public ResponseEntity<?> allcategories(String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid.");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		
		UserEntity user = userrepo.findByemailid(useremail).orElseThrow(()-> new UsernameNotFoundException("User not found"));
		if(user.getRole() != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed.");
		}
		List<Category> categories = catrepo.findAll();
		if(categories.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body("Add Category to view");
		}
		List<CategoryDTO> dto = categories.stream().map((c)->{
			String imagedata = null;
			String imagename = null;
			String imagetype = null;
			if(c.getImagedata() != null) {
				imagedata = Base64.getEncoder().encodeToString(c.getImagedata());
				imagename = c.getImagename();
				imagetype = c.getImagetype();
			}
			
			return new CategoryDTO(c.getCategoryid(),c.getCategoryname(), imagename, imagetype, imagedata);
		}).toList();
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

}
