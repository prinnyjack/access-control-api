package com.brunoams.accesscontrol.config.accounts;

import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@AllArgsConstructor
public class AdminConfig implements CommandLineRunner {

    private UserService userService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = userService.findById(1L);
        user.setUsername("admin@mail.com");
        user.setPassword("123456");
        user.setRole(Role.ADMIN);
        userService.save(user);
    }
}
