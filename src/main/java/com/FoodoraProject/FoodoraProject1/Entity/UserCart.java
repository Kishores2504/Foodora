package com.FoodoraProject.FoodoraProject1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int usercartid;
	
	@ManyToOne
//	@JsonIgnore
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@ManyToOne
//	@JsonIgnore
	@JoinColumn(name = "food_id")
	private FoodEntity food;
	
	private int count;
	private double cartamount;
	
	public UserCart() {};
	
	public UserCart(UserEntity user,FoodEntity food, int count,double cartamount) {
		this.user=user;
		this.food=food;
		this.count=count;
		this.cartamount=cartamount;
	}
	public int getUsercartid() {
		return usercartid;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public FoodEntity getFood() {
		return food;
	}
	public void setFood(FoodEntity food) {
		this.food = food;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setcartamount (double cartamount) {
		this.cartamount=cartamount;
	}
	public double getcartamount() {
		return cartamount;
	}
}
