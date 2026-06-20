package org.anonymous.af;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
public class AfPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(AfPlatformApplication.class, args);
    }
}
