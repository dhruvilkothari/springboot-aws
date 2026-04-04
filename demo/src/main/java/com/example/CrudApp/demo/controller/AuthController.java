package com.example.CrudApp.demo.controller;

import com.example.CrudApp.demo.dto.LoginDto;
import com.example.CrudApp.demo.dto.LoginResponse;
import com.example.CrudApp.demo.response.ApiResponse;
import com.example.CrudApp.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginUser(@RequestBody(required = true)LoginDto loginDto){
        return authService.loginUser(loginDto);
    }

}
