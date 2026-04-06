package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.FoodoraProject.FoodoraProject1.Entity.FoodEntity;

import jakarta.transaction.Transactional;

public interface FoodRepository extends JpaRepository<FoodEntity, Integer>{
	
	Optional<FoodEntity>findByfoodname(String foodname);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	void deleteByfoodid(int foodid);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM FoodEntity f WHERE f.category.categoryid = ?1")
	void deleteByCategoryid(int id );
}
