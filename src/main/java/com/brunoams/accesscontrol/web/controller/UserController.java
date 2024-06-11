package com.brunoams.accesscontrol.web.controller;

import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.service.UserService;
import com.brunoams.accesscontrol.web.dto.PasswordRequestDto;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> findById (@Valid @PathVariable Long id) {

        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserDto(user));
    }

    @RequestMapping("/{id}")
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> deleteById(@Valid @PathVariable Long id) {

        User user = userService.findById(id);
        userService.isAuthorized(user);
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> create(@Valid @RequestBody RegisterRequestDto dto) {

        userService.isAuthorized(UserMapper.toUser(dto));
        User user = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body((UserMapper.toUserDto(user)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordRequestDto userDTO) {
        User changedUser = userService.findById(id);
        userService.isAuthorized(changedUser);

        userService.updatePassword(id, userDTO.getCurrentPassword(),
                userDTO.getNewPassword(), userDTO.getRepeatNewPassword());

        return ResponseEntity.noContent().build();
    }
}
