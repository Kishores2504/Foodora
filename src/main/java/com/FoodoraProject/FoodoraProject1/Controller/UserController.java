package com.FoodoraProject.FoodoraProject1.Controller;

import java.io.IOException;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FoodoraProject.FoodoraProject1.DTOs.CardDetailsDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.FinalOrderDto;
import com.FoodoraProject.FoodoraProject1.DTOs.LoginDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.ProfileDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.ProfiledetailsDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.RegisterDTO;
import com.FoodoraProject.FoodoraProject1.Entity.CardDetails;
import com.FoodoraProject.FoodoraProject1.Service.UserService;

import jakarta.websocket.server.PathParam;


@RestController
@RequestMapping("/user")

public class UserController {
	@Autowired
	UserService userservice;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterDTO register){
		return userservice.register(register);
	}   
	
	@PostMapping("/login")
	public ResponseEntity<?>login(@RequestBody LoginDTO login){
		return userservice.login(login);
	}
	
	@GetMapping("/profile") 
	public ResponseEntity<?> profile(@RequestHeader("Authorization") String token){
		return userservice.profile(token);
	}
	 
	@PostMapping("/profileimage")
	public ResponseEntity<?>profileimage(
			@RequestParam(name = "profileimage",required = true)MultipartFile image,
			@RequestHeader("Authorization")String token) throws IOException{
		if(image!=null) {
		return userservice.profileimage(image,token);
			}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image is null");
		}
	
	@PostMapping("/profileupdate") 
	public ResponseEntity<?>profiledetails(@RequestBody ProfiledetailsDTO profile,
			@RequestHeader("Authorization")String token){
		return userservice.profiledetailupdate(profile,token);  
	}
	
	@PostMapping("/addcard")  
	public ResponseEntity<?>addcard(@RequestBody CardDetailsDTO card , @RequestHeader("Authorization")String token){
		return userservice.addcard(card,token);
	}
	
	@GetMapping("/cards")
	public ResponseEntity<?>getcards(@RequestHeader("Authorization") String token){
		return userservice.getcards(token);
	}
	
	@DeleteMapping("/deletecard")
	public ResponseEntity<?>deletecard(@RequestHeader("Authorization")String token,@RequestParam int cardid){
		return userservice.deletecard(token,cardid);
	}
	
	@PostMapping("/addfavourites/{foodid}")
	public ResponseEntity<?> addfavourite(@PathVariable int foodid,@RequestHeader("Authorization")String token){
		return userservice.addfavourite(foodid,token);
	}
	
	@GetMapping("/allfavourites")
	public ResponseEntity<?> allfavourites(@RequestHeader("Authorization") String token ){
		return userservice.allfavourites(token);
	}
	@PostMapping("/addcart")
	public ResponseEntity<?> addcart(@RequestParam int foodid ,@RequestHeader("Authorization")String token){
		return userservice.addcart(token,foodid);
	}
	@PostMapping("/cartupdate")
	public ResponseEntity<?>updatecart(@RequestParam int cartid,@RequestParam boolean incdec,@RequestHeader("Authorization")String token){
		return userservice.updatecart(cartid,incdec,token);
	}
	@DeleteMapping("/deletecart")
	public ResponseEntity<?>deletecart(@RequestParam int cartid,@RequestHeader("Authorization")String token){
		return userservice.deletecart(cartid,token);
	}
	@GetMapping("/cartitems")
	public ResponseEntity<?>cartitems(@RequestHeader("Authorization")String token){
		return userservice.cartitems(token);
	}
	@PostMapping("/cardpasswordcheck")
	public ResponseEntity<?>checkpasswordcard(@RequestParam long password ,@RequestParam int cardid, @RequestHeader("Authorization") String token){
		return userservice.checkpasswordcard(password,cardid,token);
	}
	@PostMapping("/placeorder")
	public ResponseEntity<?>placeorder(@RequestBody FinalOrderDto dto,@RequestHeader("Authorization") String token){
		return userservice.placeorder(dto,token);
	}
	
	@GetMapping("/userorders")
	public ResponseEntity<?>userorders(@RequestHeader("Authorization") String token){
		return userservice.getorders(token);
	} 
	
	@PutMapping("/cancelorder/{orderid}")
	public ResponseEntity<?>cancelorder(@PathVariable long orderid , @RequestHeader("Authorization") String token){
		return userservice.cancelorder(orderid,token);
	}
	@PutMapping("/changepassword")
	public ResponseEntity<?> changepassword (@RequestParam String oldpassword,@RequestParam String password , @RequestHeader("Authorization")String token){
		return userservice.changepassword(oldpassword,password , token);
	}
}
