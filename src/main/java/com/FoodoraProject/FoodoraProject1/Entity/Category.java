package com.FoodoraProject.FoodoraProject1.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int categoryid;
	private String categoryname;
	private String imagename;
	private String imagetype;
	@Lob
	private byte[] imagedata;
	
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<FoodEntity> foods;
	
	public Category() {};
	
	public Category(String categoryname,String imagename,String imagetype,byte [] imagedata) {
		this.categoryname=categoryname;
		this.imagename=imagename;
		this.imagetype=imagetype;
		this.imagedata=imagedata;
	}

	public int getCategoryid() {
		return categoryid;
	}
	
	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
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

	public List<FoodEntity> getFoods() {
		return foods;
	}

	public void setFoods(List<FoodEntity> foods) {
		this.foods = foods;
	}
	
}
