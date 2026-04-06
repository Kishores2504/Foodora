package com.FoodoraProject.FoodoraProject1.Security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.FoodoraProject.FoodoraProject1.DTOs.CardDetailsDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtility {
	
	@Value("${my.secret}")
	private String key;
	
	
	private Key getSignin() {
		 return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(String useremail) {
		return Jwts.builder()
				.setSubject(useremail)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
				.signWith(getSignin(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignin())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignin())
				.build()
				.parseClaimsJws(token)	
				.getBody();
	}
	
	public Date getExpiration(String token) {
		return getClaims(token).getExpiration();
	}
	public boolean isExpired(Date date) {
		return date.before(new Date());
	}
	public boolean isTokenValid(UserDetails user,String token) {
		try {
			String username=extractUsername(token);
			Date expiration=getExpiration(token);
			return (username.equals(user.getUsername())&& !isExpired(expiration));
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
