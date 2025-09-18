package com.adapter.contentloaderadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContentLoaderAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentLoaderAdapterApplication.class, args);
    }

}
