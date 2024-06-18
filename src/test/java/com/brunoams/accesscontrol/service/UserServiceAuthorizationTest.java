package com.brunoams.accesscontrol.service;

import com.brunoams.accesscontrol.config.security.SecurityFilter;
import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.exception.InvalidAuthorityException;
import com.brunoams.accesscontrol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class UserServiceAuthorizationTest {

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should throw InvalidAuthorityException when user is not authenticated")
    public void shouldThrowExceptionWhenUserNotAuthenticated() {
        try (MockedStatic<SecurityFilter> mockedSecurityFilter = mockStatic(SecurityFilter.class)) {
            mockedSecurityFilter.when(SecurityFilter::getCurrentUser).thenReturn(null);
            User parameterUser = new User();
            assertThrows(InvalidAuthorityException.class, () -> userService.isAuthorized(parameterUser));
        }
    }

    @Test
    @DisplayName("Should throw InvalidAuthorityException when RH tries to create/update/delete non-WORKER user")
    public void shouldThrowExceptionWhenRHTriesToModifyNonWorker() {
        try (MockedStatic<SecurityFilter> mockedSecurityFilter = mockStatic(SecurityFilter.class)) {
            User currentUser = new User();
            currentUser.setId(1L);
            currentUser.setRole(Role.RH);

            User parameterUser = new User();
            parameterUser.setId(2L);
            parameterUser.setRole(Role.ADMIN);

            mockedSecurityFilter.when(SecurityFilter::getCurrentUser).thenReturn(currentUser);
            assertThrows(InvalidAuthorityException.class, () -> userService.isAuthorized(parameterUser));
        }
    }

    @Test
    @DisplayName("Should throw InvalidAuthorityException when WORKER tries to modify another user")
    public void shouldThrowExceptionWhenWorkerTriesToModifyAnotherUser() {
        try (MockedStatic<SecurityFilter> mockedSecurityFilter = mockStatic(SecurityFilter.class)) {
            User currentUser = new User();
            currentUser.setId(1L);
            currentUser.setRole(Role.WORKER);

            User parameterUser = new User();
            parameterUser.setId(2L);
            parameterUser.setRole(Role.WORKER);

            mockedSecurityFilter.when(SecurityFilter::getCurrentUser).thenReturn(currentUser);
            assertThrows(InvalidAuthorityException.class, () -> userService.isAuthorized(parameterUser));
        }
    }

    @Test
    @DisplayName("Should not throw any exception when RH modifies WORKER user")
    public void shouldNotThrowExceptionWhenRHModifiesWorker() {
        try (MockedStatic<SecurityFilter> mockedSecurityFilter = mockStatic(SecurityFilter.class)) {
            User currentUser = new User();
            currentUser.setId(1L);
            currentUser.setRole(Role.RH);

            User parameterUser = new User();
            parameterUser.setId(2L);
            parameterUser.setRole(Role.WORKER);

            mockedSecurityFilter.when(SecurityFilter::getCurrentUser).thenReturn(currentUser);
            userService.isAuthorized(parameterUser);
        }
    }

    @Test
    @DisplayName("Should not throw any exception when user modifies themselves")
    public void shouldNotThrowExceptionWhenUserModifiesThemselves() {
        try (MockedStatic<SecurityFilter> mockedSecurityFilter = mockStatic(SecurityFilter.class)) {
            User currentUser = new User();
            currentUser.setId(1L);
            currentUser.setRole(Role.WORKER);

            User parameterUser = new User();
            parameterUser.setId(1L);
            parameterUser.setRole(Role.WORKER);

            mockedSecurityFilter.when(SecurityFilter::getCurrentUser).thenReturn(currentUser);
            userService.isAuthorized(parameterUser);
        }
    }
}

