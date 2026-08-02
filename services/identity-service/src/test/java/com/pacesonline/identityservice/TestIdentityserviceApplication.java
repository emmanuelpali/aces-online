package com.pacesonline.identityservice;

import org.springframework.boot.SpringApplication;

public class TestIdentityserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(IdentityserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
