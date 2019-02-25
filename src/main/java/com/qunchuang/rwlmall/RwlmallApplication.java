package com.qunchuang.rwlmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class RwlmallApplication {

	public static void main(String[] args) {
		SpringApplication.run(RwlmallApplication.class, args);
	}

}
