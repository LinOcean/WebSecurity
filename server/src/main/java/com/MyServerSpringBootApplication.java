package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author: ocean
 * @since: 2021-01-31
 **/

@SpringBootApplication
@ServletComponentScan(basePackages = "com.filter")
public class MyServerSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyServerSpringBootApplication.class, args);
    }
}
