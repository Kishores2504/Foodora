package com.FoodoraProject.FoodoraProject1.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtUtility jwtutil;
	
	@Autowired
	CustomUserDetailService userdetailsservice;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    String path = request.getServletPath();
	    if (path.equals("/user/register") || path.equals("/user/login")) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    String header = request.getHeader("Authorization");
	    String username = null;
	    String token = null;
	    try {
	        if (header != null && header.startsWith("Bearer ")) {
	            token = header.substring(7).trim();

	            // ✅ extra safety check
	            if (token.isEmpty() || token.equals("null")) {
	                System.out.println("⚠ Invalid token received: " + header);
	                filterChain.doFilter(request, response);
	                return;
	            }
	            username = jwtutil.extractUsername(token);
	        }
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails user = userdetailsservice.loadUserByUsername(username);
	            if (jwtutil.isTokenValid(user, token)) {
	                UsernamePasswordAuthenticationToken userpass =
	                        new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
	                userpass.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(userpass);
	            }
	        }
	    } catch (io.jsonwebtoken.MalformedJwtException e) {
	        System.out.println("❌ Malformed JWT detected: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("❌ JWT Filter Error: " + e.getMessage());
	    }
	    filterChain.doFilter(request, response);
	}
}
