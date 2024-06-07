package com.brunoams.accesscontrol.web.controller;

import com.brunoams.accesscontrol.config.security.SecurityFilter;
import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.service.UserService;
import com.brunoams.accesscontrol.web.dto.RegisterRequestDto;
import com.brunoams.accesscontrol.web.dto.UserResponseDto;
import com.brunoams.accesscontrol.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll () {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(UserMapper.findAllDTO(users));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> create(@Valid @RequestBody RegisterRequestDto dto) {
        Role currentUserRole = SecurityFilter.getCurrentUserRole();
        if (currentUserRole == Role.RH && !dto.getRole().equals(Role.WORKER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Moderators can only create users with ROLE_WORKER.");
        }
        User user = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body((UserMapper.toUserDto(user)));
    }
}
