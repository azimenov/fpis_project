package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.UserRepository;
import org.example.fpis_project.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        user.setFullname(user.getName() + " " + user.getSurname());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User updatedUser = optionalUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setSurname(user.getSurname());
            updatedUser.setFullname(user.getName() + ' ' + user.getSurname());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setBirthdate(user.getBirthdate());
            updatedUser.setGender(user.getGender());
            updatedUser.setPhoneNumber(user.getPhoneNumber());
            return userRepository.save(updatedUser);
        }
        else {
            return null;
        }
    }
}
