package com.angularPlay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan 
@SpringBootApplication
public class AngularplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngularplayApplication.class, args);
	}

}
