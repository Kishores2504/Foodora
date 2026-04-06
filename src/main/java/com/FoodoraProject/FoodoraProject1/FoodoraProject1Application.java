package com.FoodoraProject.FoodoraProject1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodoraProject1Application {	
	public static void main(String[] args) {		
		SpringApplication.run(FoodoraProject1Application.class, args);
	}
}