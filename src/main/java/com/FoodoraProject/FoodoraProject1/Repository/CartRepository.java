package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FoodoraProject.FoodoraProject1.Entity.UserCart;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<UserCart, Integer>{
	
	@Query("select c from UserCart c where c.user.userid = ?1")
	List<UserCart> findByuser(int id);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("delete from UserCart c where c.user.userid = ?1")
	void deleteByuserid(int id);
}
