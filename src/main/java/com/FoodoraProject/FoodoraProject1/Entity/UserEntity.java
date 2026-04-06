package com.FoodoraProject.FoodoraProject1.Entity;

import java.util.ArrayList; 
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	private String username;
	@Column(unique=true)
	private String emailid;
	private String password;
	private long phonenumber;
	private String address;
	private String imagename;
	private String imagetype;
	@Lob
	private byte[] imagedata;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	@JsonIgnore
	private List<CardDetails>card=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	@JsonIgnore
	private List<FinalOrder>orders=new ArrayList<FinalOrder>();
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Favourites> favourites;
	
	public UserEntity() {};
	
	public UserEntity(String username,String emailid,String password,
			long phonenumber,String address,String imagename,String imagetype,byte[] imagedata) {
		this.username=username;
		this.emailid=emailid;
		this.password=password;
		this.phonenumber=phonenumber;
		this.address=address;
		this.imagename=imagename;
		this.imagetype=imagetype;
		this.imagedata=imagedata;
	}
	public int getUserid() {
		return userid;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(long phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<CardDetails> getCard() {
		return card;
	}

	public void setCard(List<CardDetails> card) {
		this.card = card;
	} 
	public List<FinalOrder>finalorders(){
		return orders;
	}
}
