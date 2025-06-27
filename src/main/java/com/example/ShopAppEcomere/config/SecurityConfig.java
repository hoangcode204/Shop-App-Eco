package com.example.ShopAppEcomere.config;


import com.cloudinary.provisioning.Account;
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


@Configuration
@EnableWebSecurity
//phân quyền trên method
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };
    private final String[] PUBLIC_ENDPOINTS_POST = {
            "/api/v1/auth/**",
            "/api/v1/orders/statistical/year",
            "/api/v1/orders",
            "/api/v1/orders/postdetails",
            "/api/v1/users/register",
            "/api/v1/users/request-password-reset",
            "/api/v1/users/reset-password",
            "/api/v1/auth/refresh",

    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "/api/v1/categories/**",
            "/api/v1/products/**",
            "/api/v1/reviews/**",
            "/api/v1/galleries/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/orders/statistical/topproduct",
            "/ws/**"
    };
    private final String[] PUBLIC_ENDPOINTS_PUT = {

    };
    private final String[] PUBLIC_ENDPOINTS_DELETE= {

    };


    @Autowired
    private CustomJwtDecoder customJwtDecoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                                request.requestMatchers(HttpMethod.GET, "/api/v1/stripe/vnpay-return").permitAll()
                       .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
                                .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS_PUT).permitAll()
                                .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINTS_DELETE).permitAll()
                                        .requestMatchers("/ws/**").permitAll()
                                .requestMatchers(SWAGGER_WHITELIST).permitAll() // 👈 thêm dòng này
                                //phân quyền trên config/hoặc trên method
//                        .requestMatchers(HttpMethod.GET,"/users").hasRole(Account.Role.ADMIN.name())
//                        .hasAuthority("ROLE_ADMIN") //hasRole và hasAu có thể thay cho nhau
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer ->
                                        //cách cũ chưa clean dùng CustomJwtDecoder
                                        //jwtConfigurer.decoder(jwtDecoder())
                                        jwtConfigurer.decoder(customJwtDecoder)
                                                //dùng ROLE_ thay SCOPE_
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                //điểm lỗi xác thực để trả ra lỗi 401
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                );

        return httpSecurity.build();
    }


    //chuyển đổi từ SCOPE_ADMIN thành ROLE_ADMIN
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        //đã có ở auth service nên bỏ
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


    //cách cũ chưa clean dùng CustomJwtDecoder
//    @Bean
//    JwtDecoder jwtDecoder(){
//        // signerKey ở application.yaml và thuật toán mã hóa
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}