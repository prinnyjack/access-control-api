package com.brunoams.accesscontrol.service;

import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.exception.UsernameUniqueViolationException;
import com.brunoams.accesscontrol.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById (Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("user id [%s] not founded", id)));
    }

    @Transactional(readOnly = true)
    public List<User> findAll () {
        return userRepository.findAll();
    }


    @Transactional
    public User save (User user) {
        try {
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException("This email is already been used.");
        }
    }


}
