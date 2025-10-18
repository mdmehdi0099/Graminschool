package com.GraminSchool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GraminschoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraminschoolApplication.class, args);
	}

}
