package com.brunoams.accesscontrol.web.dto.mapper;

import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.web.dto.LoginRequestDto;
import com.brunoams.accesscontrol.web.dto.RegisterRequestDto;
import com.brunoams.accesscontrol.web.dto.UserResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser (LoginRequestDto userDTO) {
        return new ModelMapper().map(userDTO, User.class);
    }

    public static User toUser (RegisterRequestDto userDTO) {
        return new ModelMapper().map(userDTO, User.class);
    }


    public static UserResponseDto toUserDto (User user) {
        return new ModelMapper().map(user, UserResponseDto.class);
    }

    public static List<UserResponseDto> findAllDTO (List<User> users) {
        return users.stream().map(user -> toUserDto(user)).collect(Collectors.toList());
    }
}
