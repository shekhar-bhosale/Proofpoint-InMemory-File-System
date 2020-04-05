package com.proofpoint.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

/**
 * In Memory File System
 * */


@SpringBootApplication
public class FilesystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesystemApplication.class, args);
	}

}
