package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FoodoraProject.FoodoraProject1.Entity.FinalOrder;

@Repository
public interface FinalOrderRepository extends JpaRepository<FinalOrder, Long>{
	
	@Query("select f from FinalOrder f where f.user.userid = ?1")
	List<FinalOrder> findByUserid(int userid);
	
}
