package com.vaishnav.airlinq.flight_ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class FlightOpsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightOpsServiceApplication.class, args);
	}

}
