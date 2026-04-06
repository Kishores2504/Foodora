package com.FoodoraProject.FoodoraProject1.DTOs;

//import java.sql.Date;
import java.time.LocalDateTime;

public record FinalOrderDto(
		double totalamount,
		java.util.List<FoodFinalDetails>fooditems,
		LocalDateTime date
		) {

}
