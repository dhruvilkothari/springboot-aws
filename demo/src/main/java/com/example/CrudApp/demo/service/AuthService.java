package com.example.CrudApp.demo.service;

import com.example.CrudApp.demo.dto.LoginDto;
import com.example.CrudApp.demo.dto.LoginResponse;
import com.example.CrudApp.demo.entity.UserEntity;
import com.example.CrudApp.demo.exeception.ResourceNotFoundException;
import com.example.CrudApp.demo.repository.UserRepository;
import com.example.CrudApp.demo.response.ApiResponse;
import com.example.CrudApp.demo.util.Argon2Util;
import com.example.CrudApp.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<Object>> loginUser(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        Optional<UserEntity> userEntityOptional =  userRepository.findByEmail(email);
        if(userEntityOptional.isEmpty()){
            throw new ResourceNotFoundException("User Not found with this Email");
        }
        UserEntity user = userEntityOptional.get();
        boolean verified = Argon2Util.verify(password, user.getPassword());
        if(!verified){
            throw new ResourceNotFoundException("User Not found with this Email or Password");
        }
        String token = JwtUtil.generateToken(user.getId(), email);
        LoginResponse loginResponse = new LoginResponse(token);
        ApiResponse<Object>apiResponse = new ApiResponse<>(true, "Token", loginResponse, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
