package com.FoodoraProject.FoodoraProject1.Entity;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class FoodEntity {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int foodid;
	private String foodname;
	private String description;
	private double price;
	private String imagename;
	private String imagetype;
	@Lob
	private byte[] imagedata;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="category_id")
	private Category category;

	private int quantity;
	
	@OneToMany(mappedBy = "food", cascade = CascadeType.PERSIST)
	private List<OrderItems> orderItems;
	
	@OneToMany(mappedBy = "food" , cascade = CascadeType.ALL , orphanRemoval = true)
	private List<UserCart> carts;
	
	@OneToMany(mappedBy = "foods",cascade = CascadeType.ALL , orphanRemoval = true)
	private List<Favourites> favourites;
	
	public FoodEntity() {}; 
	public FoodEntity(String foodname,String description,long price
			,String imagename,String imagetype,byte[] imagedata,Category category,int quantity) {
		this.foodname=foodname;
		this.description=description;
		this.price=price;
		this.imagename=imagename;
		this.imagetype=imagetype;
		this.imagedata=imagedata;
		this.category=category;
		this.quantity=quantity;
		
	}
	public int getFoodid() {
		return foodid;
	}
	public String getFoodname() {
		return foodname;
	}
	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getImagename() {
		return imagename;
	}
	public void setImagename(String imagename) {
		this.imagename = imagename;
	}
	public String getImagetype() {
		return imagetype;
	}
	public void setImagetype(String imagetype) {
		this.imagetype = imagetype;
	}
	public byte[] getImagedata() {
		return imagedata;
	}
	public void setImagedata(byte[] imagedata) {
		this.imagedata = imagedata;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
//	public void setfavourite(boolean favourite) {
//		this.isfavourite=favourite;
//	}
//	public boolean getfavourite() {
//		return isfavourite;
//	}
}
