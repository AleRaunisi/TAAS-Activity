package com.TASS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.TASS.config.UploadProperties;

@SpringBootApplication  // Indica che questa è la classe principale di Spring Boot
@EnableConfigurationProperties(UploadProperties.class)
@ComponentScan(basePackages = "com.TASS")
public class TASSApplication {

    public static void main(String[] args) {
        // Avvia l'applicazione Spring Boot
        SpringApplication.run(TASSApplication.class, args);
    }
}