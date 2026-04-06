package com.FoodoraProject.FoodoraProject1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItems {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private int orderitemid;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private FinalOrder order;
	
	@ManyToOne
	@JoinColumn(name = "food_id", nullable = true)
	private FoodEntity food;
	
	private int quantity;
	private double price; 
	
	public OrderItems() {};
		
	public OrderItems(FinalOrder order,FoodEntity food,int quantity,double price) {
		this.order=order;
		this.food=food;
		this.quantity=quantity;
		this.price=price;
	}
	public int getOrderitemid() {
		return orderitemid;
	}
	public FinalOrder getOrder() {
		return order;
	}
	public void setOrder(FinalOrder order) {
		this.order = order;
	}
	public FoodEntity getFood() {
		return food;
	}
	public void setFood(FoodEntity food) {
		this.food = food;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
