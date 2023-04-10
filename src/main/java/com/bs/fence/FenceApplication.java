package com.bs.fence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FenceApplication.class, args);
    }

}
