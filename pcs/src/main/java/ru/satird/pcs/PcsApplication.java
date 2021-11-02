package ru.satird.pcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcsApplication.class, args);
	}

}
