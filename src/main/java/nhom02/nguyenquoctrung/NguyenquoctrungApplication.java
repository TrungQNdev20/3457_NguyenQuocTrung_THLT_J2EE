package nhom02.nguyenquoctrung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nhom02.nguyenquoctrung.entities.Role;
import nhom02.nguyenquoctrung.entities.User;
import nhom02.nguyenquoctrung.repositories.IRoleRepository;
import nhom02.nguyenquoctrung.repositories.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class NguyenquoctrungApplication {

	public static void main(String[] args) {
		SpringApplication.run(NguyenquoctrungApplication.class, args);
	}

	@Bean
	CommandLineRunner init(IUserRepository userRepository, IRoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Role adminRole = roleRepository.findByName("ADMIN");
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setName("ADMIN");
				adminRole.setDescription("Admin role");
				roleRepository.save(adminRole);
			}

			Role userRole = roleRepository.findByName("USER");
			if (userRole == null) {
				userRole = new Role();
				userRole.setName("USER");
				userRole.setDescription("User role");
				roleRepository.save(userRole);
			}

			User admin = userRepository.findByUsername("admin");
			if (admin == null) {
				admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("123456"));
				admin.setEmail("admin@gmail.com");
				admin.setPhone("0909090909");
				admin.setRoles(new HashSet<>(List.of(adminRole)));
				userRepository.save(admin);
			} else {
				// Ensure admin has ADMIN role matches
				if (admin.getRoles() == null) {
					admin.setRoles(new HashSet<>());
				}
				boolean hasAdminRole = admin.getRoles().stream()
						.anyMatch(r -> r.getName().equals("ADMIN"));
				if (!hasAdminRole) {
					admin.getRoles().add(adminRole);
					userRepository.save(admin);
				}
			}
		};
	}

}
