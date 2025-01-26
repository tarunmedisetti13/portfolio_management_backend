package com.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class StockPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPortfolioApplication.class, args);
	}
	@Bean
	RestTemplate restTemplate() {
	     return new RestTemplate();
	    }

}
