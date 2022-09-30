package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhiqin.zhang
 */
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class MailBatchSendingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailBatchSendingApplication.class, args);
    }
}
