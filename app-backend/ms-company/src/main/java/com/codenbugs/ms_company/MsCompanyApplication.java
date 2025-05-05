package com.codenbugs.ms_company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCompanyApplication.class, args);
	}

}
