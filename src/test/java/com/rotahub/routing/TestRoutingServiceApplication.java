package com.rotahub.routing;

import org.springframework.boot.SpringApplication;

public class TestRoutingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RoutingServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
