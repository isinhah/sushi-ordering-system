package com.sushi.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Sushi Ordering System",
		description = "API REST for managing sushi orders, customers, employees, food categories, products and menu items"))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}