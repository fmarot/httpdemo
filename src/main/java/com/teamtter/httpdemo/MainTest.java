package com.teamtter.httpdemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainTest {
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("java.io.tmpdir"));
		System.out.println(System.setProperty("java.io.tmpdir", "c:\\toto\\"));
		System.out.println(System.getProperty("java.io.tmpdir"));
		Path tempFile = Files.createTempFile("aaa", "ooo");
		System.out.println(tempFile.toString());
	}
}
