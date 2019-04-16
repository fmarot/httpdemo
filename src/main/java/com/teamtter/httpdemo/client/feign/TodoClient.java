package com.teamtter.httpdemo.client.feign;

import com.teamtter.httpdemo.common.Endpoints;

import feign.Param;
import feign.RequestLine;

public interface TodoClient {

	@RequestLine("POST " + Endpoints.TodoMethods.upload)
	byte[] upload(@Param("fileToSave") String logms, byte[] content);
}
