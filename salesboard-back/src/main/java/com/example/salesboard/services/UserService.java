package com.example.salesboard.services;

import com.example.salesboard.dtos.ChangePasswordRequest;
import com.example.salesboard.dtos.RegisterUserRequest;
import com.example.salesboard.dtos.UpdateUserRequest;
import com.example.salesboard.dtos.UserDto;
import com.example.salesboard.entities.User;
import com.example.salesboard.mappers.UserMapper;
import com.example.salesboard.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public UserDto registerUser(RegisterUserRequest request){
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        userMapper.update(request, user);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Неверный старый пароль");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Новый пароль должен отличаться от старого");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
