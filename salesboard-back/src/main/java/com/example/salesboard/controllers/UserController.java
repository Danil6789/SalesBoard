package com.example.salesboard.controllers;

import com.example.salesboard.dtos.ChangePasswordRequest;
import com.example.salesboard.dtos.RegisterUserRequest;
import com.example.salesboard.dtos.UpdateUserRequest;
import com.example.salesboard.dtos.UserDto;
import com.example.salesboard.mappers.UserMapper;
import com.example.salesboard.repositories.UserRepository;
import com.example.salesboard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private UserMapper userMapper;
    private UserRepository userRepository;
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            UriComponentsBuilder uriBuilder,
            @RequestBody RegisterUserRequest request
    ){

        UserDto userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    )
    {
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ){

        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id){
        var user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
