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
                .email(request.getEmail())
                .fullname(request.getUsername())
                .password(request.getPassword())
                .build();

        userRepository.save(user);
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Invalid credentials");
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}
