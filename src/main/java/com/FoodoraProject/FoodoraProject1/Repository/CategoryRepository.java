package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FoodoraProject.FoodoraProject1.Entity.Category;

import jakarta.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
	Optional<Category>findBycategoryname(String categoryname);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("delete from Category c where c.categoryname=?1")
	void Deletebycategoryname(String categoryname);
}
