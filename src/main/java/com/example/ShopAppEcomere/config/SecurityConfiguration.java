package com.example.ShopAppEcomere.config;

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
public class SecurityConfiguration {
    private final Map<HttpMethod, String[]> PUBLIC_ENDPOINTS = Map.of(
        HttpMethod.POST, new String[]{
                "api/v1/users/register",

                "api/v1/auth/token",
                "api/v1/auth/introspect",
                "api/v1/auth/logout",
                    "api/v1/auth/refresh",
                    "api/v1/auth/outbound/authentication",
        },
        HttpMethod.GET, new String[]{
                "api/v1/products/**",
                "api/v1/categories/**",
                    "api/v1/users/myInfo"
            }
    );
    private final Map<HttpMethod, String[]> ADMIN_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
//                    "api/v1/users/**",
                    "api/v1/shipments/**",
                    "api/v1/orderitems/**",
                    "api/v1/orders/**",
                    "api/v1/permissions",
                    "api/v1/payments/**",
                    "api/v1/carts/**"
            },
            HttpMethod.DELETE, new String[]{
                    "api/v1/users/**",
                    "api/v1/shipments/**",
                    "api/v1/products/**",
                    "api/v1/orderitems/**",
                    "api/v1/orders/**",
                    "api/v1/categories/**",
                    "api/v1/permissions/**"
            },
            HttpMethod.PUT, new String[]{
                    "api/v1/shipments/**",
                    "api/v1/products/**",
                    "api/v1/categories/**"
            },
            HttpMethod.POST, new String[]{
                    "api/v1/products",
                    "api/v1/categories",
                    "api/v1/permissions",
                    "api/v1/roles"
            }
    );
    private final Map<HttpMethod, String[]> USER_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
                    "api/v1/users/{userId}",
                    "api/v1/shipments/{shipmentId}",
                    "api/v1/payments/{paymentId}",
                    "api/v1/orderitems/{orderItemId}",
                    "api/v1/orders/{orderId}",
                    "api/v1/carts/{cartId}",
                    "api/v1/orders/users/{userId}"

            },
            HttpMethod.DELETE, new String[]{
                    "api/v1/users/{userId}",
                    "api/v1/shipments/{shipmentId}",
                    "api/v1/payments/{paymentId}",
                    "api/v1/orderitems/{orderItemId}",
                    "api/v1/carts/{cartId}"
            },
            HttpMethod.PUT, new String[]{
                    "api/v1/users/{userId}",
                    "api/v1/shipments/{shipmentId}",
                    "api/v1/payments/{paymentId}",
                    "api/v1/orderitems/{orderItemId}",
                    "api/v1/orders/{orderId}",
                    "api/v1/carts/{cartId}"
            },
            HttpMethod.POST, new String[]{
                    "api/v1/shipments",
                    "api/v1/payments",
                    "api/v1/orderitems",
                    "api/v1/orders",
                    "api/v1/files/upload",
                    "api/v1/files/download",
                    "api/v1/carts",
                    "api/v1/users/create-password"
            }
    );

    @Autowired
    private CustomJwtDecoder customJwtDecoder;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {//định nghĩa luồng bảo mật cho ứng dụng
        httpSecurity.authorizeHttpRequests(request -> {
            // 1. Public Endpoints: Cho phép không cần xác thực
            PUBLIC_ENDPOINTS.forEach((method, endpoints) -> {
                for (String endpoint : endpoints) {
                    request.requestMatchers(method, endpoint).permitAll();
                }
            });
            // 3. User Endpoints: Chỉ cho phép user truy cập
            USER_ENDPOINTS.forEach((method, endpoints) -> {
                for (String endpoint : endpoints) {
                    request.requestMatchers(method, endpoint).hasRole("USER");
                }
            });
            // 2. Admin Endpoints: Chỉ cho phép admin truy cập
            ADMIN_ENDPOINTS.forEach((method, endpoints) -> {
                for (String endpoint : endpoints) {
                    request.requestMatchers(method, endpoint).hasRole("ADMIN");
                }
            });


            // 3. Các request khác phải xác thực
            request.anyRequest().authenticated();
        });

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)//sử dụng customJwtDecoder để giải mã JWT (JSON Web Token).
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))//Chuyển đổi token JWT thành thông tin xác thực của người dùng.
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())//Xử lý lỗi khi người dùng không có quyền truy cập hoặc token không hợp lệ

        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//        jwtAuthenticationConverter.setPrincipalClaimName("userId"); // Thêm dòng này để ánh xạ userId
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

}