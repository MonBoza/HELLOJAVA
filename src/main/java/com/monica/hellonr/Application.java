package com.monica.hellonr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = "com.monica.hellonr")
public class Application {
 public static void main(String[] args) {
   SpringApplication.run(Application.class, args);
 }
}