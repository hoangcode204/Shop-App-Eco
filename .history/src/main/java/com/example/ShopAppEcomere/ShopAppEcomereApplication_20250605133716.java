package com.example.ShopAppEcomere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.ShopAppEcomere.repository")
@EnableFeignClients
@ComponentScan(basePackages = {"com.example.ShopAppEcomere"})
public class ShopAppEcomereApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAppEcomereApplication.class, args);
	}

}
