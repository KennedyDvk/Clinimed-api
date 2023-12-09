package br.com.clinimed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class ClinimedApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinimedApplication.class, args);
	}

}
