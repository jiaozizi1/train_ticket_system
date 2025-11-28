package org.example.stationticketsystem;

import org.example.stationticketsystem.entity.User;
import org.example.stationticketsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StationTicketSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(StationTicketSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// 创建管理员用户
			if (!userRepository.existsByUsername("admin")) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setEmail("admin@example.com");
				admin.setRole("ADMIN");
				userRepository.save(admin);
				System.out.println("管理员用户创建成功: admin/admin123");
			}

			// 创建测试用户
			if (!userRepository.existsByUsername("test")) {
				User test = new User();
				test.setUsername("test");
				test.setPassword(passwordEncoder.encode("test123"));
				test.setEmail("test@example.com");
				test.setRole("USER");
				userRepository.save(test);
				System.out.println("测试用户创建成功: test/test123");
			}
		};
	}
}