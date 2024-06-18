package com.brunoams.accesscontrol.service;

import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.exception.UsernameUniqueViolationException;
import com.brunoams.accesscontrol.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        User user = new User(1L, "bruno@gmail.com", "123456");
        lenient().when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        lenient().when(userRepository.findUserByUsername("bruno@gmail.com")).thenReturn(Optional.of(user));
        lenient().when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        lenient().when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(2L);
            return savedUser;
        });
    }

    @Test
    @DisplayName("Must return a list of Users")
    public void mustReturnListOfUsers() {
        List<User> users = userService.findAll();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    @DisplayName("Must return a User by Id")
    public void mustReturnUserById() {
        User findedUser = userService.findById(1L);

        Assertions.assertEquals(1L, findedUser.getId());
        Assertions.assertEquals("bruno@gmail.com", findedUser.getUsername());
        Assertions.assertEquals("123456", findedUser.getPassword());
    }

    @Test
    @DisplayName("Must return a UsernameUniqueViolationException when the username already exists in database.")
    public void mustReturnUsernameUniqueViolationException() {
        User newUserWithSameUsername = new User(null, "bruno@gmail.com", "123456");
        Assertions.assertThrows(UsernameUniqueViolationException.class,
                () -> userService.save(newUserWithSameUsername));
    }

    @Test
    @DisplayName("Must create a new user.")
    public void mustCreateUser() {
        User newUser = new User(null, "joaozinho@gmail.com", "123456");
        User savedUser = userService.save(newUser);
        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("joaozinho@gmail.com", savedUser.getUsername());
        Assertions.assertEquals("encodedPassword", savedUser.getPassword());

    }
}
