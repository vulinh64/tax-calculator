package com.vulinh;

import com.vulinh.security.properties.SecurityConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
