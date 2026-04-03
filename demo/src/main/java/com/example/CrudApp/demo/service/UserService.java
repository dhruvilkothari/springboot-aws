package com.example.CrudApp.demo.service;

import com.example.CrudApp.demo.dto.UserDto;
import com.example.CrudApp.demo.entity.UserEntity;
import com.example.CrudApp.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserEntity> userEntities  = userRepository.findAll();
        List<UserDto> userDtos = userEntities.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class)).toList();

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    public ResponseEntity<UserDto> createUser(UserDto userDto){
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userRepository.save(userEntity);
        return  new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
