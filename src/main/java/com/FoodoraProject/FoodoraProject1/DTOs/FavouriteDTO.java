package com.FoodoraProject.FoodoraProject1.DTOs;

import java.util.List;

import com.FoodoraProject.FoodoraProject1.Entity.FoodEntity;

public record FavouriteDTO(int userid,List<FoodEntity>foods) {

}
