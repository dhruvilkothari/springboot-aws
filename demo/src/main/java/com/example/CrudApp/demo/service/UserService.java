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
import java.util.Optional;

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

    public ResponseEntity<UserDto> getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        UserDto userDto = modelMapper.map(userEntity.get(), UserDto.class);
        return  new ResponseEntity<>(userDto, HttpStatus.FOUND);
    }
}
