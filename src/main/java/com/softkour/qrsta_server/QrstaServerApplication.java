package com.softkour.qrsta_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication()
@EnableCaching

public class QrstaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrstaServerApplication.class, args);
	}

}
