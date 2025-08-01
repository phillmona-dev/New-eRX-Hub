package com.medco.eprescription_out_of_stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EprescriptionOutOfStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(EprescriptionOutOfStockApplication.class, args);
	}

}
