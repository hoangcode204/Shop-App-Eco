package com.example.ShopAppEcomere.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    // 🔓 PUBLIC ENDPOINTS - Không cần xác thực
    private final Map<HttpMethod, String[]> PUBLIC_ENDPOINTS = Map.of(
            HttpMethod.POST, new String[]{
                    "api/v1/users/register",
                    "api/v1/auth/token",
                    "api/v1/auth/introspect",
                    "api/v1/auth/logout",
                    "api/v1/auth/refresh",
                    "api/v1/auth/outbound/authentication"
            },
            HttpMethod.GET, new String[]{
                    "api/v1/products",
                    "api/v1/products/{productId}",
                    "api/v1/categories",
                    "api/v1/categories/{id}"
            }
    );

    // 🔐 ADMIN ENDPOINTS - Chỉ ADMIN được truy cập
    private final Map<HttpMethod, String[]> ADMIN_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
                    "api/v1/orders",
                    "api/v1/orders/statistical/**",
                    "api/v1/orders/details",
                    "api/v1/permissions",
                    "api/v1/roles"
            },
            HttpMethod.POST, new String[]{
                    "api/v1/products",
                    "api/v1/categories",
                    "api/v1/permissions",
                    "api/v1/roles"
            },
            HttpMethod.PUT, new String[]{
                    "api/v1/products/{productId}",
                    "api/v1/categories/{id}",
                    "api/v1/orders/shipping",
                    "api/v1/orders/complete"
            },
            HttpMethod.DELETE, new String[]{
                    "api/v1/products/{productId}",
                    "api/v1/categories/{id}",
                    "api/v1/permissions/{id}",
                    "api/v1/roles/{id}"
            }
    );

    // 🔐 USER ENDPOINTS - Chỉ USER được truy cập
    private final Map<HttpMethod, String[]> USER_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
                    "api/v1/users/{userId}",
                    "api/v1/orders/user/{id}",
                    "api/v1/orders/{id}"
            },
            HttpMethod.POST, new String[]{
                    "api/v1/orders",
                    "api/v1/orders/postdetails",
                    "api/v1/users/change-password"
            },
            HttpMethod.PUT, new String[]{
                    "api/v1/users/{userId}",
                    "api/v1/orders/cancel"
            },
            HttpMethod.DELETE, new String[]{
                    "api/v1/users/{userId}"
            }
    );

    private final CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> {
            // 🔓 1. Cho phép truy cập Swagger UI mà không cần xác thực
            request.requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs",
                    "/webjars/**"
            ).permitAll();

            // 🔓 2. Public Endpoints: Không yêu cầu xác thực
            PUBLIC_ENDPOINTS.forEach((method, endpoints) -> {
                for (String endpoint : endpoints) {
                    request.requestMatchers(method, endpoint).permitAll();
                }
            });

            // 🔐 3. User Endpoints: Chỉ USER được truy cập
            USER_ENDPOINTS.forEach((method, endpoints) -> {
                for (String endpoint : endpoints) {
                    request.requestMatchers(method, endpoint).hasRole("USER");
                }
            });

            // 🔐 4. Admin Endpoints: Chỉ ADMIN được truy cập
            ADMIN_ENDPOINTS.forEach((method, endpoints) -> {
                for (String endpoint : endpoints) {
                    request.requestMatchers(method, endpoint).hasRole("ADMIN");
                }
            });

            // 🔐 5. Các request khác phải xác thực
            request.anyRequest().authenticated();
        });

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
