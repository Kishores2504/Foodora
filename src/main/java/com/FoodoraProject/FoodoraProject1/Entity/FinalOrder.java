package com.FoodoraProject.FoodoraProject1.Entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class FinalOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderid;
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
	private List<OrderItems> items=new ArrayList<OrderItems>();
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	@Enumerated(EnumType.STRING)
	private Status status;
	private double totalamount;
	private String address;
	private LocalDateTime orderDateTime;
	
	public FinalOrder(List<OrderItems> items,UserEntity user,Status status,double totalamount,String address) {
		this.items=items;
		this.user=user;
		this.status=status;
		this.totalamount=totalamount;
		this.address=address;
	}
    public FinalOrder() {
        // Automatically set the current time when object is created
        this.orderDateTime = LocalDateTime.now();
    }
    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
	public long getOrderid() {
		return orderid;
	}
	public List<OrderItems> getItems() {
		return items;
	}
	public void setItems(List<OrderItems> items) {
		this.items = items;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	public String getaddress() {
		return address;
	}
	public void setaddress(String address) {
		this.address=address;
	}
}
