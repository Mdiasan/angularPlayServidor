package com.angularPlay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan 
@SpringBootApplication
//spring.datasource.url= jdbc:mysql://localhost:3306/angularplay?serverTimezone=Europe/Madrid
//spring.datasource.username=angularplay
//spring.datasource.password=1234
//spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
//spring.jpa.database-platform = org.hibernate.dialect.MySQL5Dialect
//spring.jpa.generate-ddl=true
//spring.jpa.hibernate.ddl-auto = update
//<dependency>
//<groupId>mysql</groupId>
//<artifactId>mysql-connector-java</artifactId>
//<scope>runtime</scope>
//</dependency>
public class AngularplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngularplayApplication.class, args);
	}

}
