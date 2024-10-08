package com.dahl.webstockmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class WebStockManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebStockManagerApplication.class, args);
    }

}
