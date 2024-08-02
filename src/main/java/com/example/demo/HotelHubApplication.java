package com.example.demo;	

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication
@EnableCaching
@EnableScheduling
public class HotelHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelHubApplication.class, args);
		
		
		
		
		
	}
	

}
 