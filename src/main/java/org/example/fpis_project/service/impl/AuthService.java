package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.LoginRequest;
import org.example.fpis_project.model.dto.RegisterRequest;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest request) {
        User user = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .fullname(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByFullname(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Invalid credentials");
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}
