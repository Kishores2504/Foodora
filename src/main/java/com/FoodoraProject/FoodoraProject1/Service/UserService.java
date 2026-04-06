	package com.FoodoraProject.FoodoraProject1.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.FoodoraProject.FoodoraProject1.DTOs.CardDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.CardDetailsDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.CartDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.FinalOrderDto;
import com.FoodoraProject.FoodoraProject1.DTOs.FoodFinalDetails;
import com.FoodoraProject.FoodoraProject1.DTOs.LoginDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.ProfileDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.ProfiledetailsDTO;
import com.FoodoraProject.FoodoraProject1.DTOs.RegisterDTO;
import com.FoodoraProject.FoodoraProject1.Entity.CardDetails;
import com.FoodoraProject.FoodoraProject1.Entity.Favourites;
import com.FoodoraProject.FoodoraProject1.Entity.FinalOrder;
import com.FoodoraProject.FoodoraProject1.Entity.FoodEntity;
import com.FoodoraProject.FoodoraProject1.Entity.OrderItems;
import com.FoodoraProject.FoodoraProject1.Entity.Role;
import com.FoodoraProject.FoodoraProject1.Entity.Status;
import com.FoodoraProject.FoodoraProject1.Entity.UserCart;
import com.FoodoraProject.FoodoraProject1.Entity.UserEntity;
import com.FoodoraProject.FoodoraProject1.Repository.CardRepository;
import com.FoodoraProject.FoodoraProject1.Repository.CartRepository;
import com.FoodoraProject.FoodoraProject1.Repository.FavouritesRepository;
import com.FoodoraProject.FoodoraProject1.Repository.FinalOrderRepository;
import com.FoodoraProject.FoodoraProject1.Repository.FoodRepository;
import com.FoodoraProject.FoodoraProject1.Repository.UserRepository;
import com.FoodoraProject.FoodoraProject1.Security.CustomUserDetailService;
import com.FoodoraProject.FoodoraProject1.Security.JwtUtility;

@Service
public class UserService {
	
	@Autowired
	UserRepository userrepo;
	@Autowired
	CardRepository cardrepo;
	@Autowired
	FoodRepository foodrepo;
	@Autowired
	PasswordEncoder passwordencode;
	@Autowired
	FavouritesRepository favrepo;
	@Autowired
	CartRepository cartrepo;
	@Autowired
	FinalOrderRepository orderrepo;
	
	@Autowired 
	JwtUtility jwtutil;
	
	@Autowired
	AuthenticationManager authmanager;
	
	@Autowired
	CustomUserDetailService userdetailservice;
	

	public ResponseEntity<?> register(RegisterDTO register) {
		// TODO Auto-generated method stub
		Optional<UserEntity> isuser=userrepo.findByemailid(register.useremail());
		if(isuser.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login Email already exist.");
		}
		if(register.useremail()==null && register.username()==null && register.userpassword()==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fill the requirements.");
		}
		UserEntity user=new UserEntity();
		user.setUsername(register.username());
		String encodedpassword=passwordencode.encode(register.userpassword());
		user.setPassword(encodedpassword);
		user.setEmailid(register.useremail());
		user.setRole(Role.USER);
		userrepo.save(user);
		return ResponseEntity.status(HttpStatus.OK).body("Register Success.");
	}

	public ResponseEntity<?> login(LoginDTO login) {
		// TODO Auto-generated method stub
		Optional<UserEntity> isuser=userrepo.findByemailid(login.useremail());
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Register to login.");
		}
		authmanager.authenticate(new UsernamePasswordAuthenticationToken
				(login.useremail(), 
				login.userpassword()));
		final UserDetails user=userdetailservice.loadUserByUsername(login.useremail());	
		String token=jwtutil.generateToken(user.getUsername());
		String role = user.getAuthorities()
					  .iterator()
					  .next()
					  .getAuthority();
//		System.out.println(role);
		if(role.startsWith("ROLE_")) {
			role = role.substring(5);
		}
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("role",role,"token",token));
	}
	
	public ResponseEntity<?> profile(String token){
		
		if(token==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("You are not allowed");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		Optional<UserEntity>isuser=userrepo.findByemailid(useremail);
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
		}
		UserEntity user=isuser.get();
		String imagedata= null;
		
		if(user.getImagedata()!= null) {
			imagedata=Base64.getEncoder().encodeToString(user.getImagedata());
		}
		ProfileDTO dto=new ProfileDTO(user.getUsername(),
				user.getEmailid(),
				user.getPhonenumber(), 
				user.getAddress(), 
				user.getImagename(), 
				user.getImagetype(), 
				imagedata
				);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(dto);
	}
	
	public ResponseEntity<?> profiledetailupdate(ProfiledetailsDTO profile,String token) {
		// TODO Auto-generated method stub 
		 if(token==null || !token.startsWith("Bearer ")) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed");
		 }
		 if(profile==null ) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter values to update.");
		 } 
		 String tokenn=token.substring(7);
		 String useremail=jwtutil.extractUsername(tokenn);
		 
		 Optional<UserEntity> isuser=userrepo.findByemailid(useremail);
		 if(isuser.isEmpty()) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
		 }
		 UserEntity user=isuser.get(); 
		 user.setAddress(profile.address()!=null && !profile.address().isEmpty() ? profile.address() : user.getAddress());
		 user.setUsername(profile.username()!=null && !profile.username().isEmpty() ? profile.username() : user.getUsername());
		 user.setPhonenumber(profile.phone()!=null ? Long.valueOf(profile.phone()) : Long.valueOf(user.getPhonenumber()));
		 userrepo.save(user);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	public ResponseEntity<?> profileimage(MultipartFile image, String token) throws IOException {
		// TODO Auto-generated method stub
		if(token==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed.");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		
		Optional<UserEntity> isuser=userrepo.findByemailid(useremail);
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
		}
		UserEntity user=isuser.get();
		user.setImagename(image.getOriginalFilename());
		user.setImagetype(image.getContentType());
		user.setImagedata(image.getBytes());
		userrepo.save(user);
		return ResponseEntity.status(HttpStatus.OK).body("Profile updated Succesfully.");
	}
 
	
	
	
	public ResponseEntity<?> addcard(CardDetailsDTO card,String token) {
		if(token==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you are not allowed.");
		}
		System.out.println(card);
//		int digit=(int)Math.log10(card.cardnumber())+1;
//		if(digit <9 || digit >16) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cardnumber should be in range of 9 to 16.");
//		}
//		if(card.bankname()==null || card.bankname().isEmpty()|| card.cardnumber()==0 || card.pinnumber()==0) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add the requirements");
//		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		
		Optional<UserEntity>isuser=userrepo.findByemailid(useremail);
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
		}
		UserEntity user=isuser.get();
		
		Optional<List<CardDetails>> cards=cardrepo.findByuserid(user.getUserid());
		//optional content will not return null it will return empty obj or value not null check it with is empty or present
//		if(cards.isEmpty()) {
//			cardrepo.save(card); 
//			return ResponseEntity.status(HttpStatus.OK).body("Card details added.");
//		}
		
		List<CardDetails> existingcards = cards.get();
		
		boolean iscardexist=existingcards.stream()
							.anyMatch(c->c.getCardnumber()==card.cardnumber());
		if(iscardexist) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card already exist.");
		}
		CardDetails cardd=new CardDetails();
	
		cardd.setBankname(card.bankname());
		cardd.setCardnumber(card.cardnumber());
		cardd.setPinnumber(passwordencode.encode(String.valueOf(card.pinnumber())));
		cardd.setUser(user);
		cardrepo.save(cardd);
		return ResponseEntity.status(HttpStatus.OK).body(cardd);
	}

	public ResponseEntity<?> getcards(String token) {
		// TODO Auto-generated method stub
		if(token==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		
		Optional<UserEntity>isuser=userrepo.findByemailid(useremail);
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}
		UserEntity user=isuser.get();
		
		Optional<List<CardDetails>>iscards=cardrepo.findByuserid(user.getUserid());
		
		if(iscards.isEmpty() || iscards.get().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add a card to view.");
		}
		List<CardDetails>cards=iscards.get();
		
		List<CardDTO> listcards=cards.stream()
								.map(c-> new CardDTO(c.getid(),c.getBankname(),c.getCardnumber()))
								.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(listcards);
	}

	public ResponseEntity<?> deletecard(String token,int cardid) {
		// TODO Auto-generated method stub
		if(token==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed");
		}
		cardrepo.deleteById(cardid);
		return ResponseEntity.status(HttpStatus.OK).body("Success.");
	}

	public ResponseEntity<?> addfavourite(int foodid, String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ") ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
//		Optional<UserEntity> isuser=userrepo.findByemailid(useremail);
//		Optional<FoodEntity>isfood=foodrepo.findById(foodid);	
		try {
			UserEntity user= userrepo.findByemailid(useremail).orElseThrow(()->new UsernameNotFoundException("User not found"));
			FoodEntity food=foodrepo.findById(foodid).orElseThrow();
			int userid=user.getUserid();
			List<Favourites> isfavourite = favrepo.findByuser_id(userid).orElse(new ArrayList<>());
			
			Optional<Favourites> existingfav = isfavourite.stream()
											.filter(f->f.getFood().getFoodid() == food.getFoodid())
											.findFirst();
			if(existingfav.isPresent()) {
				favrepo.delete(existingfav.get());
				return ResponseEntity.status(HttpStatus.OK).body("Food Removed from favourites.");
			}
			else {
				Favourites newfavourite= new Favourites();
				newfavourite.setFoods(food);
				newfavourite.setUser(user);
				favrepo.save(newfavourite);
				return ResponseEntity.status(HttpStatus.OK).body("Food Added to favourites");
			}
		} 
		catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in adding and removing.");
		}
	}

	public ResponseEntity<?> allfavourites(String token) {
		// TODO Auto-generated method stub
		if( token ==null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		
		Optional<UserEntity> isuser=userrepo.findByemailid(useremail);
		if(isuser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not found");
		}
		UserEntity user=isuser.get();
		List<Favourites> isfavourite= favrepo.findByuser_id(user.getUserid()).orElse(new ArrayList<>());
		if(isfavourite.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Content");
		}
		return ResponseEntity.status(HttpStatus.OK).body(isfavourite);
	}

	public ResponseEntity<?> addcart(String token, int foodid) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Invalid.");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		
		UserEntity user= userrepo.findByemailid(useremail).orElseThrow(()->new UsernameNotFoundException("User not found"));
		FoodEntity food=foodrepo.findById(foodid).orElseThrow(()-> new UsernameNotFoundException("Food id not found"));
		
		List<UserCart> iscart= cartrepo.findByuser(user.getUserid());

		Optional<UserCart> existcartitem=iscart.stream().filter((c)-> c.getFood().getFoodid() == foodid).findFirst();
		
		if(existcartitem.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Increase the count in cart section");
		}
		else {
			UserCart newcart= new UserCart();
			newcart.setCount(1);
			newcart.setFood(food);
			newcart.setUser(user);
			newcart.setcartamount(food.getPrice()*1);
			cartrepo.save(newcart);
		}	
		return ResponseEntity.ok("Food added to cart successfully");
	}

	public ResponseEntity<?> updatecart(int cartid, boolean incdec, String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Invalid");
		}
		String tokenn=token.substring(7);
		String useremail=jwtutil.extractUsername(tokenn);
		
		UserCart cart = cartrepo.findById(cartid).orElseThrow(()-> new UsernameNotFoundException("Cart item not found"));
		FoodEntity food = foodrepo.findByfoodname(cart.getFood().getFoodname()).orElseThrow(()->new UsernameNotFoundException("Food not found"));
		if(cart.getCount() == 1 && incdec ==  false) {
			cartrepo.deleteById(cartid);
			return ResponseEntity.status(HttpStatus.OK).body("Cart item deleted.");
		}
		if(incdec) {
			cart.setCount(cart.getCount()+1);
			cart.setcartamount(food.getPrice()*cart.getCount());
			cartrepo.save(cart);
			CartDTO dto = new CartDTO(food, cart.getCount(), cartid,cart.getcartamount());
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
		else {
			cart.setCount(cart.getCount()-1);
			cart.setcartamount(food.getPrice()*cart.getCount());
			cartrepo.save(cart);
			CartDTO dto = new CartDTO(food, cart.getCount(), cartid,cart.getcartamount());
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
	}

	public ResponseEntity<?> deletecart(int cartid, String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
		}
	    String jwt = token.substring(7); // remove "Bearer "
	    String username;
	    try {
	        username = jwtutil.extractUsername(jwt);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
	    }
		Optional<UserCart> cart =  cartrepo.findById(cartid);
		if(cart.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add Cart item to delete.");
		}
		cartrepo.deleteById(cartid);
		return ResponseEntity.status(HttpStatus.OK).body("Deleted cart item.");
	}

	public ResponseEntity<?> cartitems(String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		UserEntity user =  userrepo.findByemailid(useremail).orElseThrow(()-> new UsernameNotFoundException("User not found"));
		List<UserCart> islist = cartrepo.findByuser(user.getUserid());
		if(islist.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
		}
		List<CartDTO> cartdto=islist.stream().map((c)->{
			double price = c.getFood().getPrice()*c.getCount();
			CartDTO dto=new CartDTO(c.getFood(),c.getCount(),c.getUsercartid(),price);
			return dto;
		}).toList();
		return ResponseEntity.status(HttpStatus.OK).body(cartdto);
	}

	public ResponseEntity<?> checkpasswordcard(long password, int cardid,String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.OK).body("Token invalid");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		
		UserEntity user= userrepo.findByemailid(useremail).orElseThrow(()->new UsernameNotFoundException("User Not found."));
		CardDetails card = cardrepo.findById(cardid).orElseThrow(()-> new UsernameNotFoundException("Card not found"));
		
		if(card.getUser().getUserid() != user.getUserid()) {
			return ResponseEntity.status(HttpStatus.OK).body("Not a Proper Request");
		}
		
		boolean ismatching = passwordencode.matches(String.valueOf(password), card.getPinnumber());
		
		if(ismatching) 
			return ResponseEntity.status(HttpStatus.OK).body(true);
		else
			return ResponseEntity.status(HttpStatus.OK).body(false);
	}

	public ResponseEntity<?> placeorder(FinalOrderDto dto, String token) {
		try {
			if(token == null || !token.startsWith("Bearer ")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Invalid");
			}
			String tokenn = token.substring(7);
			String useremail = jwtutil.extractUsername(tokenn);
			UserEntity user = userrepo.findByemailid(useremail).orElseThrow(()-> new UsernameNotFoundException("User not Found."));
			String address = user.getAddress();
			
			FinalOrder finalorder  = new FinalOrder();
			finalorder.setUser(user);
			finalorder.setStatus(Status.ordered);
			finalorder.setTotalamount(dto.totalamount());
			finalorder.setaddress(address);
			
			List<OrderItems> orderitems = new ArrayList<OrderItems>();
			
			for(FoodFinalDetails finaldetail : dto.fooditems()) {
				
				FoodEntity foods = foodrepo.findById(finaldetail.foodid()).orElseThrow(()-> new RuntimeException("Food id not found :"+ finaldetail.foodid()));
				OrderItems items = new OrderItems();
				items.setFood(foods);
				items.setOrder(finalorder);
				items.setPrice(finaldetail.price());
				items.setQuantity(finaldetail.quantity());
				
				orderitems.add(items);
			}
			finalorder.setItems(orderitems);
			FinalOrder orderdetail =orderrepo.save(finalorder);
			cartrepo.deleteByuserid(user.getUserid());
			return ResponseEntity.status(HttpStatus.OK).body(orderdetail);
		} catch (Exception e) {
			// TODO: handle exception
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "Order creation failed", "details", e.getMessage()));
		}
	}

	public ResponseEntity<?> getorders(String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		
		UserEntity user = userrepo.findByemailid(useremail).orElseThrow(()-> new UsernameNotFoundException("User Not Found."));
		List<FinalOrder> orderlist = orderrepo.findByUserid(user.getUserid());
		return ResponseEntity.status(HttpStatus.OK).body(orderlist);
	}
	

	public ResponseEntity<?> cancelorder(long orderid, String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
		}
		String tokenn = token.substring(7);
		FinalOrder order = orderrepo.findById(orderid).orElseThrow(()-> new RuntimeException("Order not found"));
		
		if(order.getStatus() != Status.ordered) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't cancel order is already "+ order.getStatus());
		}
		
		order.setStatus(Status.canceled);
		orderrepo.save(order);
		return ResponseEntity.status(HttpStatus.OK).body(order);
	}

	public ResponseEntity<?> changepassword(String oldpassword ,String password, String token) {
		// TODO Auto-generated method stub
		if(token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Invalid.");
		}
		String tokenn = token.substring(7);
		String useremail = jwtutil.extractUsername(tokenn);
		
		UserEntity user = userrepo.findByemailid(useremail).orElseThrow(()-> new UsernameNotFoundException("User not found."));
		
		if(!passwordencode.matches(oldpassword,user.getPassword())) {
			return ResponseEntity.status(HttpStatus.OK).body("Incorrect Password.");
		}
		String newpassword = passwordencode.encode(password);
		user.setPassword(newpassword);
		userrepo.save(user);
		return ResponseEntity.status(HttpStatus.OK).body("Password Changed Succesfully.");
	}
	
}
