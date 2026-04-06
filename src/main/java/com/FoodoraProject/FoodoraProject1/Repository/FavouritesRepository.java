package com.FoodoraProject.FoodoraProject1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FoodoraProject.FoodoraProject1.Entity.Favourites;

@Repository
public interface FavouritesRepository extends JpaRepository<Favourites, Integer>{
	
	
	@Query("select f from Favourites f where f.user.userid = ?1")
	Optional<List<Favourites>> findByuser_id(int userid);
}
