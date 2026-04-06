package com.FoodoraProject.FoodoraProject1.DTOs;

public record ProfileDTO(
		String username,
		String useremail,
		long phone,
		String address,
		String imagename,
		String imagetype,
		String imagedata) {}
