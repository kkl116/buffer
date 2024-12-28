package com.gubu.buffer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class Buffer {
    public static void main(String[] args) {
        SpringApplication.run(Buffer.class, args);
    }
}
