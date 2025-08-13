package com.monica.hellonr;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
 @GetMapping("/")
 public String root() {
   return "App is up. Try /hello or /slow";
 }
 @GetMapping("/hello")
 public String hello() {
   return "Hello from Spring Boot!";
 }
 @GetMapping("/slow")
 public String slow() throws InterruptedException {
   Thread.sleep(2000);
   return "That took a second...";
 }
 @GetMapping("/error-demo")
 public String errorDemo() {
    throw new RuntimeException("Intentional demo error for testing");
 }
}
