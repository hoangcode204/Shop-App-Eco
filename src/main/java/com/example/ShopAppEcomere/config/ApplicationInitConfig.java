package com.example.ShopAppEcomere.config;
import com.example.ShopAppEcomere.entity.Role;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.enums.RoleEnum;
import com.example.ShopAppEcomere.repository.RoleRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            // 🔹 Tìm Role ADMIN trong database
            Role adminRole = roleRepository.findByName("ADMIN");

            // 🔹 Nếu chưa có thì tạo mới
            if (adminRole == null) {
                adminRole = Role.builder().name("ADMIN").build();
                roleRepository.save(adminRole);
                log.info("Đã tạo mới Role 'ADMIN' trong database.");
            }

            // 🔹 Kiểm tra nếu User admin chưa tồn tại thì tạo mới
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User user = User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin")) // ✅ Mật khẩu đã được mã hóa
                        .roles(Set.of(adminRole)) // ✅ Đã chắc chắn có Role ADMIN
                        .build();

                userRepository.save(user);
                log.info("Đã tạo User 'admin@gmail.com' với mật khẩu mặc định: admin");
            } else {
                log.warn("Tài khoản admin đã tồn tại, bỏ qua việc tạo mới.");
            }
        };
    }
}