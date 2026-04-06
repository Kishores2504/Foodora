package com.FoodoraProject.FoodoraProject1.Entity;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CardDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private long cardnumber;
	private String bankname;
	private String pinnumber;
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private UserEntity user;
	
	public CardDetails() {};
	
	public CardDetails(Long cardnumber,String bankname,
			String pinnumber,UserEntity user) {
		this.cardnumber=cardnumber;
		this.bankname=bankname;
		this.pinnumber=pinnumber;
		this.user=user;
	}
	public int getid() {
		return id;
	}
	public void setCardnumber(long cardnumber) {
		this.cardnumber=cardnumber;
	}
	public long getCardnumber() {
		return cardnumber;
	}
	public void setBankname(String bankname) {
		this.bankname=bankname;
	}
	public String getBankname() {
		return bankname;
	}
	public void setPinnumber(String pinnumber) {
		this.pinnumber=pinnumber;
	}
	public String getPinnumber() {
		return pinnumber;
	}
	public void setUser(UserEntity user) {
		this.user=user;
	}
	public UserEntity getUser() {
		return user;
	}
}
