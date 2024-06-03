package com.brunoams.accesscontrol.web.controller;

import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.service.UserService;
import com.brunoams.accesscontrol.web.dto.UserRequestDto;
import com.brunoams.accesscontrol.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequestDto dto) {
        User user = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body((UserMapper.toUserDto(user)));
    }
}
