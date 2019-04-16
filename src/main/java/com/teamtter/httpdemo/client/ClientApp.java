package com.teamtter.httpdemo.client;

import com.teamtter.httpdemo.client.feign.TodoClient;
import com.teamtter.httpdemo.common.Endpoints;
import com.teamtter.httpdemo.server.ServerApp;

import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientApp {

	static String serverBase = "http://127.0.0.1:8080/";

	public static void main(String[] args) throws IOException {

		HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
			@Override
			public void log(String message) {
				// TODO: accumulate message to log effectively a single time
				log.info(message);
			}
		};
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
		logging.setLevel(Level.BODY);

		OkHttpClient client = new OkHttpClient().newBuilder()
				.addInterceptor(logging)
				.build();

		byte[] contentToSend = "da content !!".getBytes();
		log.info("Raw: " + sendRaw(client, contentToSend));
		log.info("Feign: " + sendFeign(client, contentToSend));
	}

	private static String sendFeign(OkHttpClient client, byte[] contentToSend) {
		TodoClient todoFeignClient = Feign.builder()
				.client(new feign.okhttp.OkHttpClient(client))
				// .decoder(new GsonDecoder())
				.target(TodoClient.class, serverBase + Endpoints.todo);
		byte[] received = todoFeignClient.upload("fileName.txt", contentToSend);
		return new String(received, StandardCharsets.UTF_8);
	}

	private static String sendRaw(OkHttpClient client, byte[] contentToSend) throws IOException {
		RequestBody body = RequestBody.create(
				MediaType.parse(APPLICATION_OCTET_STREAM_VALUE), contentToSend);

		Request request = new Request.Builder()
				.url(serverBase + Endpoints.todo + Endpoints.TodoMethods.upload)
				.post(body)
				.build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
}
