package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.*;
import com.example.ShopAppEcomere.dto.response.auth.AuthenticationResponse;
import com.example.ShopAppEcomere.dto.response.auth.IntrospectResponse;
import com.example.ShopAppEcomere.entity.InvalidatedToken;
import com.example.ShopAppEcomere.entity.Role;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.UserMapper;
import com.example.ShopAppEcomere.repository.InvalidatedTokenRepository;
import com.example.ShopAppEcomere.repository.RoleRepository;
import com.example.ShopAppEcomere.repository.UserRepository;

import com.example.ShopAppEcomere.repository.httpclient.OutboundShopAppClient;
import com.example.ShopAppEcomere.repository.httpclient.OutboundUserClient;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    OutboundShopAppClient outboundShopAppClient;
    OutboundUserClient outboundUserClient;
    RoleRepository roleRepository;
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid_duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;
    /////
    @NonFinal
    @Value("${outbound.shopapp.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.shopapp.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.shopapp.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token,false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
    public AuthenticationResponse outboundAuthenticate(String code) {
        var response = outboundShopAppClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        log.info("TOKEN RESPONSE {}", response);

        // Get user info by send access token
        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("User Info {}", userInfo);

//        Set<Role> roles = new HashSet<>();
//        roles.add(Role.builder().name(PredefinedRole.USER_ROLE).build());
        Role userRole = roleRepository.findByName("USER");

        // Onboard user
        var user = userRepository
                .findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .fullName(userInfo.getGivenName())
                        .roles(Set.of(userRole))
                        .build()));

        // Generate token
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Tạo access và refresh token
        var accessToken = generateToken(user);
        var refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toUserResponse(user))
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(),true);//kiểm tra tính hợp lệ token

        String jit = signToken.getJWTClaimsSet().getJWTID();//lấy mã định danh token
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();//lấy tg hết hạn token

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
    private String generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("huyhoang.com")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 ngày
                .jwtID(UUID.randomUUID().toString())
                .claim("type", "refresh") // Phân biệt token refresh
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create refresh token", e);
            throw new RuntimeException(e);
        }
    }
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);//Tạo header

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())//lấy mail làm sub
                .issuer("huyhoang.com")
                .issueTime(new Date())//thời gian phát hành token
                .expirationTime(new Date(System.currentTimeMillis() + VALID_DURATION * 1000))
                .jwtID(UUID.randomUUID().toString())//mã định danh id
                .claim("scope", buildScope(user))
                .claim("userId", user.getId()) // Thêm userId vào token
                .claim("email",user.getEmail())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());//tạo payload và kí token

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // 1. Xác minh và vô hiệu hóa refresh token cũ
        var signedJWT = verifyToken(request.getToken(), true); // 'true' để xác minh là refresh token

        if (!"refresh".equals(signedJWT.getJWTClaimsSet().getClaim("type"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED); // Đảm bảo đúng loại token
        }

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Vô hiệu hóa refresh token cũ ngay lập tức sau khi sử dụng
        invalidatedTokenRepository.save(
                InvalidatedToken.builder()
                        .id(jit)
                        .expiryTime(expiryTime)
                        .build()
        );

        // 2. Tìm người dùng từ email trong refresh token cũ
        var email = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // 3. Tạo cặp token MỚI (access token và refresh token mới)
        var newAccessToken = generateToken(user);        // Tạo access token mới
        var newRefreshToken = generateRefreshToken(user); // ✅ Tạo refresh token MỚI

        // 4. Trả về cả access token MỚI và refresh token MỚI
        return AuthenticationResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken) // ✅ BẮT BUỘC phải trả về refresh token MỚI ở đây
                .authenticated(true)
                .user(userMapper.toUserResponse(user))
                .build();
    }

    private SignedJWT verifyToken(String token,boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); //khơỉ tạo đối tượng xác minh token

        SignedJWT signedJWT = SignedJWT.parse(token);//đối tượng đại diện cho JWT đã ký

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();//lấy thời gian hết hạn

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");//StringJoiner giúp nối các chuỗi với dấu cách (" ").

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}