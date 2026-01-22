package com.example.salesboard.services;

import com.example.salesboard.mappers.UserMapper;
import com.example.salesboard.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
