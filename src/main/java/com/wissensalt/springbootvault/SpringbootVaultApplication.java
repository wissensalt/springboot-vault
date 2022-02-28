package com.wissensalt.springbootvault;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@EnableConfigurationProperties(VaultSecret.class)
@SpringBootApplication
public class SpringbootVaultApplication implements CommandLineRunner {

	private final EmployeeRepository employeeRepository;

	private final VaultSecret vaultSecret;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootVaultApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info(">>>>> Start Seeding Database");
		if (employeeRepository.count() > 0) {
			return;
		}

		final Faker faker = new Faker();
		final List<Employee> employees = new ArrayList<>();
		for (int a=0; a<10; a++) {
			employees.add(Employee.builder()
					.firstName(faker.name().firstName())
					.lastName(faker.name().lastName())
					.identityNumber(faker.idNumber().valid())
					.build());
		}
		employeeRepository.saveAll(employees);
		log.info(">>>>> Finished Seeding Database");
	}

	@PostMapping("/employee")
	private Employee createEmployee(@RequestBody RequestCreateEmployee request) {

		return employeeRepository.save(Employee.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.identityNumber(request.getIdentityNumber())
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