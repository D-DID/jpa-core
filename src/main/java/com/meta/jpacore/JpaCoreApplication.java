package com.meta.jpacore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JpaCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaCoreApplication.class, args);
    }

}
