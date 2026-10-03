package com.lifecare.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LifeCareHospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeCareHospitalApplication.class, args);
    }
}
