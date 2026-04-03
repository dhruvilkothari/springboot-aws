package com.example.CrudApp.demo.controller;

import com.example.CrudApp.demo.dto.UserDto;
import com.example.CrudApp.demo.response.ApiResponse;
import com.example.CrudApp.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/getAllUsers")
    public ResponseEntity<ApiResponse<Object>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody(required = true) UserDto userDto){
        return userService.createUser(userDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

}
