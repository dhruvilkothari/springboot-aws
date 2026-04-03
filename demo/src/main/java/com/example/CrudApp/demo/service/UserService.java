package com.example.CrudApp.demo.service;

import com.example.CrudApp.demo.dto.UserDto;
import com.example.CrudApp.demo.entity.UserEntity;
import com.example.CrudApp.demo.exeception.ResourceNotFoundException;
import com.example.CrudApp.demo.repository.UserRepository;
import com.example.CrudApp.demo.response.ApiResponse;
import com.example.CrudApp.demo.util.Argon2Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public ResponseEntity<ApiResponse<Object>> getAllUsers() {

        List<UserEntity> userEntities  = userRepository.findAll();
        List<UserDto> userDtos = userEntities.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class)).toList();

        ApiResponse<Object> apiResponse = new ApiResponse<>(true, "Got All Users", userDtos, null);
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<ApiResponse<Object>> createUser(UserDto userDto){
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        String hashedPassword = Argon2Util.hash(userDto.getPassword());
        userEntity.setPassword(hashedPassword);

        userRepository.save(userEntity);
        ApiResponse<Object>apiResponse = new ApiResponse<>(true, "User Created", userDto, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    public ResponseEntity<ApiResponse<Object>> getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        UserDto userDto = modelMapper.map(userEntity.get(), UserDto.class);
        return  ResponseEntity.ok(new ApiResponse<>(true, "User Found With Given Id", userDto, null));
    }
}
