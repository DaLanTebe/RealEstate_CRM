package com.adapter.contentprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContentProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentProcessorApplication.class, args);
    }

}
