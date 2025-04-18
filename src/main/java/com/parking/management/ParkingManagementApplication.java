package com.parking.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.parking.management")
@EntityScan("com.parking.management.model")
@EnableJpaRepositories("com.parking.management.repository")
public class ParkingManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkingManagementApplication.class, args);
    }
}