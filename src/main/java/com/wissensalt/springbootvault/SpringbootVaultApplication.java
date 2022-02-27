package com.wissensalt.springbootvault;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.vault.config.Secrets;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@SpringBootApplication
public class SpringbootVaultApplication {

	private final EmployeeRepository employeeRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootVaultApplication.class, args);
	}

	@GetMapping("/")
	private List<Employee> getEmployees() {
		Secrets secrets = new Secrets();
		return employeeRepository.findAll();
	}
}
