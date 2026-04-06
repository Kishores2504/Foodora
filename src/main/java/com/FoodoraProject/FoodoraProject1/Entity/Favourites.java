package com.FoodoraProject.FoodoraProject1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Favourites {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int favouriteid;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "food_id")
	private FoodEntity foods ;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	public Favourites() {};
	
	public Favourites(FoodEntity foods,UserEntity user) {
		this.foods=foods;
		this.user=user;
	}
	public void setFoods(FoodEntity food) {
		this.foods=food;
	}
	public FoodEntity getFood() {
		return foods;
	}
	public void setUser(UserEntity user) {
		this.user=user;
	}
	public UserEntity getUser() {
		return user;
	}
}
