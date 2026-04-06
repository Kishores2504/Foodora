package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.FoodoraProject.FoodoraProject1.Entity.OrderItems;

import jakarta.transaction.Transactional;

public interface OrderItemRepository extends JpaRepository<OrderItems, Integer>{
	
	@Query("select o from OrderItems o where o.food.foodid = ?1")
	List<OrderItems> existbyfoodid(int foodid);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM OrderItems o WHERE o.food.foodid = ?1")
	void deleteByfoodid(int foodid);
}
