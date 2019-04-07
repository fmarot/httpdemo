package com.teamtter.httpdemo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ServerApp {

	public static void main(String[] args) {
		SpringApplication.run(ServerApp.class, args);
		log.info("Server Main done");
	}
	
}