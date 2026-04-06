package com.FoodoraProject.FoodoraProject1.DTOs;

import org.springframework.web.multipart.MultipartFile;

public record EditFoodDTO(int id, String foodname, String description, double price,
		String categoryname, MultipartFile image) {}
