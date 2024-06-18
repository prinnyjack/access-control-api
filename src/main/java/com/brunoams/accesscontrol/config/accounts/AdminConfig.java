package com.brunoams.accesscontrol.config.accounts;

import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Configuration
@AllArgsConstructor
public class AdminConfig implements CommandLineRunner {

    private UserService userService;

//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        User user = userService.findById(1L);
//        user.setUsername("admin@mail.com");
//        user.setPassword("123456");
//        user.setRole(Role.ADMIN);
//        userService.save(user);
//    }
//@Override
//@Transactional
//public void run(String... args) throws Exception {
//    String username = "admin@mail.com";
//    if (!userService.isUsernameAlreadyTaken(username)) {
//        userService.deleteById(1L);
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("123456");
//        user.setRole(Role.ADMIN);
//        userService.save(user);
//    }
//}

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<User> list = userService.findAll();
        if(list.isEmpty()) {
            User newUser = new User();
            newUser.setUsername("admin@gmail.com");
            newUser.setPassword("123456");
            newUser.setRole(Role.ADMIN);
            userService.save(newUser);
        }
    }
}
