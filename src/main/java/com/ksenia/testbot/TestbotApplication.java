package com.ksenia.testbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication

public class TestbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestbotApplication.class, args);
	}
}
