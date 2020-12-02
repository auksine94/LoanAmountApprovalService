package com.bank.loanamountapprovalservice;

import com.bank.loanamountapprovalservice.model.Contract;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.SpringApplication;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class LaasApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaasApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build();
	}

	@Bean
	public List<Contract> contractsList() {
		return new ArrayList<>();
	}
}
