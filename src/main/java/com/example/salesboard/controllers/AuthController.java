package com.example.salesboard.controllers;

import com.example.salesboard.config.JwtConfig;
import com.example.salesboard.dtos.JwtResponse;
import com.example.salesboard.dtos.LoginRequest;
import com.example.salesboard.dtos.RegisterRequest;
import com.example.salesboard.dtos.UserDto;
import com.example.salesboard.mappers.UserMapper;
import com.example.salesboard.repositories.UserRepository;
import com.example.salesboard.services.AuthService;
import com.example.salesboard.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var user = authService.getCurrentUser();
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value="refreshToken") String refreshToken
    ){
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
