package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.PasswordCreationRequest;
import com.example.ShopAppEcomere.dto.request.UserRequest;
import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.ResetPasswordToken;
import com.example.ShopAppEcomere.entity.Role;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.UserMapper;
import com.example.ShopAppEcomere.repository.ResetPasswordTokenRepository;
import com.example.ShopAppEcomere.repository.RoleRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    CloudinaryImageService cloudinaryImageService;
    ResetPasswordTokenRepository resetPasswordTokenRepository;
    EmailService emailService;
    private static final long RESET_TOKEN_EXPIRATION_MINUTES = 15; // 15 phút

    public List<UserResponse> getAll() {
        List<User> users= userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    public UserResponse getUserByEmailAndPassword(String email, String password) {
        return userMapper.toUserResponse(userRepository.findUserByEmailAndPassword(email, password));
    }

    public UserResponse createUser(UserRequest request){
        // **1.Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        // **2. Chuyển đổi UserRequest thành User**
        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // **3. Gán quyền USER cho user mới**
        Role userRole = roleRepository.findByName("USER");
        newUser.setRoles(Set.of(userRole));

        // **4. Lưu vào database**
        User savedUser = userRepository.save(newUser);

        return userMapper.toUserResponse(savedUser);
    }
    public UserResponse changePassword(Integer id, PasswordCreationRequest passwordCreationRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(passwordCreationRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(passwordCreationRequest.getNewPassword()));
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }
    public UserResponse getUserById(Integer id){
        User user=userRepository.findByUserId(id);
        if(user==null){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse update(Integer userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra nếu request != null thì mới cập nhật dữ liệu của user
        if (request != null) {
            user.setUsername(request.getUsername());
            user.setFullName(request.getFullName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setGender(request.getGender());
            user.setDate_of_birth(request.getDate_of_birth());
            user.setImg(request.getImg()); // 💡 Đảm bảo dòng này tồn tại!
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse deleteUser(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Date currenDate = new Date();
        user.setDeletedAt(currenDate);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    @Transactional
    public String generateAndSaveResetToken(User user) {
        String token = UUID.randomUUID().toString();
        ResetPasswordToken resetToken = ResetPasswordToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRATION_MINUTES))
                .build();

        resetPasswordTokenRepository.save(resetToken);
        return token;
    }
    public void handlePasswordResetRequest(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo và lưu token dưới transaction
        String token = generateAndSaveResetToken(user);

        // Gửi email (không nằm trong transaction)
        sendResetEmail(user, token);
    }
    private void sendResetEmail(User user, String token) {
        String resetLink = "http://localhost:8086/reset-password?token=" + token;
        String subject = "Yêu cầu đặt lại mật khẩu";
        String body = "Xin chào " + user.getFullName() + ",\n\n" +
                "Bạn hoặc ai đó đã yêu cầu đặt lại mật khẩu cho tài khoản của bạn.\n" +
                "Vui lòng truy cập đường link sau để đặt lại mật khẩu (hết hạn trong 15 phút):\n" +
                resetLink + "\n\n" +
                "Nếu bạn không yêu cầu, hãy bỏ qua email này.";

        try {
            emailService.sendEmail(subject, user.getEmail(), body);
        } catch (MessagingException e) {
            // Ghi log, không throw ra ngoài để không ảnh hưởng user experience
            log.error("Lỗi khi gửi email đặt lại mật khẩu tới {}: {}", user.getEmail(), e.getMessage());
        }
    }
    // --- Bước 2: Reset mật khẩu bằng token ---
    public UserResponse resetPassword(String token, String newPassword) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_RESET_TOKEN));

        // Kiểm tra token hết hạn chưa
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.RESET_TOKEN_EXPIRED);
        }

        User user = resetToken.getUser();
        log.info("Token reset password: {}", token);

        // Cập nhật mật khẩu mới đã mã hóa
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xoá hoặc đánh dấu token đã dùng
        resetPasswordTokenRepository.delete(resetToken);

        log.info("Reset mật khẩu thành công cho user: {}", user.getEmail());

        return userMapper.toUserResponse(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }


}