package com.example.salesboard.controllers;

import com.example.salesboard.dtos.ChangePasswordRequest;
import com.example.salesboard.dtos.RegisterUserRequest;
import com.example.salesboard.dtos.UpdateUserRequest;
import com.example.salesboard.dtos.UserDto;
import com.example.salesboard.entities.User;
import com.example.salesboard.mappers.UserMapper;
import com.example.salesboard.repositories.UserRepository;
import com.example.salesboard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private UserMapper userMapper;
    private UserRepository userRepository;
    private UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            UriComponentsBuilder uriBuilder,
            @RequestBody RegisterUserRequest request
    ){
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        UserDto userDto = userMapper.toDto(user);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    )
    {
        var user = userRepository.findById(id).orElse(null);
        if (user == null){
           return ResponseEntity.notFound().build();
        }
        userMapper.update(request, user);
        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ){

        var user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        if(!user.getPassword().equals(request.getOldPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.notFound().build();
        } catch(EmptyResultDataAccessException e){
            return ResponseEntity.noContent().build();
        }
    }
}
