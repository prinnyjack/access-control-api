package com.brunoams.accesscontrol.web.controller;

import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.service.UserService;
import com.brunoams.accesscontrol.web.dto.PasswordRequestDto;
import com.brunoams.accesscontrol.web.dto.RegisterRequestDto;
import com.brunoams.accesscontrol.web.dto.UserResponseDto;
import com.brunoams.accesscontrol.web.dto.mapper.UserMapper;
import com.brunoams.accesscontrol.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations related to user management")
public class UserController {

    private final UserService userService;


    @Operation(summary = "Get a list of all Users.", description = "Return a list of all users.\n" +
            "* You must log-in to do this.", responses = {
            @ApiResponse(responseCode = "200", description = "All users found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied, you must log-in.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll () {
        
        List<User> users = userService.findAll();
        return ResponseEntity.ok(UserMapper.findAllDTO(users));
    }


    @Operation(summary = "Get user by ID", description = "Returns the details of a user by their ID.\n" +
            "* You must log-in to do this.", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "resource not found due to invalid param",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied, you must log-in.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> findById (@Valid @PathVariable Long id) {

        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserDto(user));
    }


    @Operation(summary = "Delete a user", description = "Delete a user with the provided details\n" +
            "* You must put the id to be deleted in the box.\n" +
            "* You have to be ADMIN or MODERATOR to do this.\n" +
            "* Moderator can delete only Workers and themselves", responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "There is no Users with this id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied, you must have permission.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@Valid @PathVariable Long id) {

        User user = userService.findById(id);
        userService.isAuthorized(user);
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details\n" +
            "* Password must have between 6-12 characters.\n" +
            "* Username must be a email [string@string.string]\n" +
            "* You have to be ADMIN or MODERATOR to do this.", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied, you must have permission.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RegisterRequestDto dto) {

        userService.isAuthorized(UserMapper.toUser(dto));
        User user = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body((UserMapper.toUserDto(user)));
    }


    @Operation(summary = "Update a User password", description = "Update a user password with the provided details\n" +
            "* You must type the current password.\n" +
            "* New password must have between 6-12 characters.\n" +
            "* You must insert the new password again.\n" +
            "* You have to be ADMIN or MODERATOR to do this.", responses = {
            @ApiResponse(responseCode = "204", description = "Password successfully updated.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "There is no Users with this id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "resource not processed due to invalid data. password must contain 6-12 characters.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "resource not processed due to invalid data. Current password is not correct.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied, you must have permission. [ADMIN, MODERATOR]",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordRequestDto userDTO) {
        User changedUser = userService.findById(id);
        userService.isAuthorized(changedUser);

        userService.updatePassword(id, userDTO.getCurrentPassword(),
                userDTO.getNewPassword(), userDTO.getRepeatNewPassword());

        return ResponseEntity.noContent().build();
    }
}
