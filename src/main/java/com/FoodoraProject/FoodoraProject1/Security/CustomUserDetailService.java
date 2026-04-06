package com.FoodoraProject.FoodoraProject1.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.FoodoraProject.FoodoraProject1.Entity.UserEntity;
import com.FoodoraProject.FoodoraProject1.Repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	UserRepository userrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity user=userrepo.findByemailid(username)
				.orElseThrow(()-> new UsernameNotFoundException("User Not found."));
		return new CustomUserDetails(user);
	}
	
	public boolean userExist(String username) {
		return userrepo.findByemailid(username).isPresent();
	}

}
