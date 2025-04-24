package com.project.readers_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReadersCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadersCommunityApplication.class, args);
    }

}
