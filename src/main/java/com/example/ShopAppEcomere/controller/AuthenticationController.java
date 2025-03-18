package com.example.ShopAppEcomere.controller;
import com.example.ShopAppEcomere.dto.request.AuthenticationRequest;
import com.example.ShopAppEcomere.dto.request.IntrospectRequest;
import com.example.ShopAppEcomere.dto.request.LogoutRequest;
import com.example.ShopAppEcomere.dto.request.RefreshRequest;
import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.auth.AuthenticationResponse;
import com.example.ShopAppEcomere.dto.response.auth.IntrospectResponse;
import com.example.ShopAppEcomere.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/outbound/authentication")
    ApiResponse<AuthenticationResponse> outboundAuthenticate(@RequestParam("code") String code) {
        log.info("Huy hoang dep trai");
        var result = authenticationService.outboundAuthenticate(code);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }
}
