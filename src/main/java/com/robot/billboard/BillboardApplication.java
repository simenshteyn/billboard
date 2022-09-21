package com.robot.billboard;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Billboard API", version = "0.1", description = "Api for billboard developers"))
public class BillboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillboardApplication.class, args);
	}
}
