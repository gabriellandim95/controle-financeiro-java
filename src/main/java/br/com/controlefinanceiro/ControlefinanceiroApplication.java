package br.com.controlefinanceiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControlefinanceiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlefinanceiroApplication.class, args);
	}

}
