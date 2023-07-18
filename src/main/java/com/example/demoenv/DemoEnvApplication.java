package com.example.demoenv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoEnvApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoEnvApplication.class, args);
	}

	private final ConfigurableApplicationContext context;

	@Value("${my.variable}")
	private String myVariable;

	public DemoEnvApplication(ConfigurableApplicationContext context) {
		this.context = context;
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello World!");
		System.out.println(myVariable);
		context.close();
	}
}
