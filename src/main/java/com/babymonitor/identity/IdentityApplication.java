package com.babymonitor.identity;

import com.babymonitor.identity.services.DotEnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdentityApplication {

	public static void main(String[] args) {

		DotEnvLoader.loadEnv();
		SpringApplication.run(IdentityApplication.class, args);
	}

}
