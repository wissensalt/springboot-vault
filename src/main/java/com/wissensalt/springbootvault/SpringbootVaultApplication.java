package com.wissensalt.springbootvault;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@EnableConfigurationProperties(VaultSecret.class)
@SpringBootApplication
public class SpringbootVaultApplication {

	private final EmployeeRepository employeeRepository;

	private final VaultSecret vaultSecret;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootVaultApplication.class, args);
	}

	@PostMapping("/employee")
	private Employee createEmployee(@RequestBody RequestCreateEmployee request) {

		return employeeRepository.save(Employee.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.build());
	}

	@GetMapping("/employee")
	private List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/secrets")
	private VaultSecret getVaultSecret() {
		return vaultSecret;
	}
}
