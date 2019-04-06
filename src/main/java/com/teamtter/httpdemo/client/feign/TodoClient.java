package com.teamtter.httpdemo.client.feign;

import com.teamtter.httpdemo.common.Endpoints;

import feign.RequestLine;

public interface TodoClient {

	@RequestLine("POST " + Endpoints.TodoMethods.upload)
	byte[] upload(byte[] content);
}
