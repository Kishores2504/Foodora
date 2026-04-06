package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FoodoraProject.FoodoraProject1.Entity.CardDetails;

import jakarta.transaction.Transactional;

@Repository
public interface CardRepository extends JpaRepository<CardDetails, Integer>{
	
	@Query("select c from CardDetails c where c.user.userid=?1")
	Optional<List<CardDetails>>findByuserid(int id);
	
	
//	@Transactional
//	@Modifying(clearAutomatically = true)
//	void deleteByid(int id);
}
