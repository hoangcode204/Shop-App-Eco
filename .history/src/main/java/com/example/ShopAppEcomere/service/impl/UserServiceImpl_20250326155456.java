package com.example.ShopAppEcomere.service.impl;

import com.example.ShopAppEcomere.entity.Role;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.UserMapper;
import com.example.ShopAppEcomere.repository.RoleRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import com.example.ShopAppEcomere.service.UserService;
import com.example.ShopAppEcomere.service.dto.UserRequest;
import com.example.ShopAppEcomere.service.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse createUser(User request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setAddress(request.getAddress());
        newUser.setDateOfBirth(request.getDateOfBirth());
        newUser.setFacebookAccountId(request.getFacebookAccountId());
        newUser.setGoogleAccountId(request.getGoogleAccountId());
        newUser.setActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        // Lấy RoleEnum USER và thêm vào danh sách roles của user
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        newUser.setRoles(Set.of(userRole));  // Set mặc định chỉ có role USER

        // Lưu user mới
        User savedUser = userRepository.save(newUser);
        return userMapper.toUserResponse(savedUser);
    }
} 