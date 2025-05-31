package com.example.voebb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VoebbApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoebbApplication.class, args);
	}

}
