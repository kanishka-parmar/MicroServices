package com.example.cartAndOrder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CartAndOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartAndOrderApplication.class, args);
	}

}

