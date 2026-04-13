package com.example.demochatgpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class DemochatgptApplication {

	@Value("${server.port:8080}")
	private String port;

	public static void main(String[] args) {
		SpringApplication.run(DemochatgptApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logSwaggerUrl() {
		String baseUrl = "http://localhost:" + port;

		System.out.println("=================================");
		System.out.println("Swagger UI: " + baseUrl + "/swagger-ui/index.html");
		System.out.println("API Docs:   " + baseUrl + "/v3/api-docs");
		System.out.println("=================================");
	}

}
