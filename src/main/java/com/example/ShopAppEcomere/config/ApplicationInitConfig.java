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

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Builder
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if (userRepository.findByPhoneNumber("0123456789").isEmpty()){
                Role adminRole = roleRepository.findByName("ADMIN");
                User user = User.builder()
                        .phoneNumber("0123456789")
                        .password(passwordEncoder.encode("admin"))
                         .roles(Set.of(adminRole))
                        .build();

                userRepository.save(user);
                log.warn("Admin user with phone number {} has been created with default password: admin, please change it", user.getPhoneNumber());
            }else {
                log.warn("Account admin is existed");
            }
        };
    }
}