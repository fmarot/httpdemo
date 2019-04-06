package com.teamtter.httpdemo.server.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import static com.teamtter.httpdemo.common.Endpoints.*;
import com.teamtter.httpdemo.common.Endpoints;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Endpoints.todo)
public class TodoController {

	/** @return the same content received in order for the caller to check what was sent 
	 * is the same as what is received (usefull for tests) */
	@PostMapping(Endpoints.TodoMethods.upload)
	public byte[] uploadFile(@RequestBody byte[] bytes) {
		String fileContent = new String(bytes, StandardCharsets.UTF_8);
		log.info("received file content: " + fileContent);
		return fileContent.getBytes();
	}
	
	
}
