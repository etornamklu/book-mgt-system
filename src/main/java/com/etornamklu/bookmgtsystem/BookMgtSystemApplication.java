package com.etornamklu.bookmgtsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookMgtSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMgtSystemApplication.class, args);
    }

}
