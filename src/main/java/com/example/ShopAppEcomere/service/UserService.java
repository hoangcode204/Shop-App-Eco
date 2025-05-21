package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.PasswordCreationRequest;
import com.example.ShopAppEcomere.dto.request.UserRequest;
import com.example.ShopAppEcomere.dto.response.UserResponse;
import com.example.ShopAppEcomere.entity.Role;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.UserMapper;
import com.example.ShopAppEcomere.repository.RoleRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public List<UserResponse> getAll() {
        List<User> users= userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    public UserResponse getUserByEmailAndPassword(String email, String password) {
        return userMapper.toUserResponse(userRepository.findUserByEmailAndPassword(email, password));
    }

    public UserResponse createUser(UserRequest request, MultipartFile file){
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // **1. Upload ảnh lên Cloudinary**
        String imageUrl = cloudinaryImageService.upload(file);

        // **2. Gán URL ảnh vào UserRequest**
        request.setImg(imageUrl);

        // **3. Chuyển đổi UserRequest thành User**
        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // **4. Gán quyền USER cho user mới**
        Role userRole = roleRepository.findByName("USER");
        newUser.setRoles(Set.of(userRole));

        // **5. Lưu vào database**
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

    public UserResponse update(Integer userId, UserRequest request, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra nếu request != null thì mới cập nhật dữ liệu của user
        if (request != null) {
            user.setUsername(request.getUsername());
            user.setFullName(request.getFullName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setGender(request.getGender());
            user.setDate_of_birth(request.getDate_of_birth());
        }

        // Nếu có file ảnh thì upload lên Cloudinary
        if (file != null && !file.isEmpty()) {
            String url = cloudinaryImageService.upload(file);
            user.setImg(url);  // Cập nhật URL ảnh mới
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

}