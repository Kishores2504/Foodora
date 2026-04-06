package com.FoodoraProject.FoodoraProject1.DTOs;

import com.FoodoraProject.FoodoraProject1.Entity.FoodEntity;
import com.FoodoraProject.FoodoraProject1.Entity.UserCart;

public record CartDTO(FoodEntity food, 
					 int count,
					 int cartid,
					 double cartamount
					 ) {

}
