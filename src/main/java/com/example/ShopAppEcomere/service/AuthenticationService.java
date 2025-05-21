package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.*;
import com.example.ShopAppEcomere.dto.response.auth.AuthenticationResponse;
import com.example.ShopAppEcomere.dto.response.auth.IntrospectResponse;
import com.example.ShopAppEcomere.entity.InvalidatedToken;
import com.example.ShopAppEcomere.entity.Role;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
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
        User user = userRepository.findByEmail(request.getUsername())
                .orElseGet(() -> userRepository.findByEmail(request.getUsername())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
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

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);//Tạo header

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())//lấy mail làm sub
                .issuer("huyhoang.com")
                .issueTime(new Date())//thời gian phát hành token
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))//tg hết hạn token
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
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();//id của token
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();//tgian hết hạn của token

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

//        var phoneNumber = signedJWT.getJWTClaimsSet().getSubject();
        var email=signedJWT.getJWTClaimsSet().getSubject();

        var user =
                userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
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