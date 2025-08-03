package com.swarga.Kartwala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KartwalaApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(KartwalaApplication.class);
	public static void main(String[] args) {
		LOGGER.info("Kartwala Application is starting...");
		SpringApplication.run(KartwalaApplication.class, args);
	}

}
