package com.brunoams.accesscontrol.service;

import com.brunoams.accesscontrol.config.security.SecurityFilter;
import com.brunoams.accesscontrol.domain.Role;
import com.brunoams.accesscontrol.domain.User;
import com.brunoams.accesscontrol.exception.EntityNotFoundException;
import com.brunoams.accesscontrol.exception.InvalidAuthorityException;
import com.brunoams.accesscontrol.exception.PasswordInvalidException;
import com.brunoams.accesscontrol.exception.UsernameUniqueViolationException;
import com.brunoams.accesscontrol.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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
        Optional<User> foundedUser = userRepository.findUserByUsername(user.getUsername());
        if(foundedUser.isPresent()) {
            throw new UsernameUniqueViolationException("This email is already been used.");
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            return userRepository.save(user);
        }
    }

    @Transactional
    public void updatePassword (Long id, String currentPassword, String newPassword, String repeatNewPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordInvalidException("Current password incorrect!");
        }
        if (!newPassword.equals(repeatNewPassword)) {
            throw new PasswordInvalidException("New passwords do not match, try again.");
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }


    @Transactional
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        userRepository.delete(user.get());
    }

    public void isAuthorized(User parameterUser) {
        User currentUser = SecurityFilter.getCurrentUser();

        if (currentUser == null) {
            throw new InvalidAuthorityException("User is not authenticated.");
        }

        Role currentUserRole = currentUser.getRole();

        if (currentUserRole == Role.RH &&
                (!currentUser.getId().equals(parameterUser.getId()) &&
                        !parameterUser.getRole().equals(Role.WORKER))) {
            throw new InvalidAuthorityException("Moderators can only create/update/delete users with ROLE_WORKER.");
        } else if (currentUserRole == Role.WORKER && !currentUser.getId().equals(parameterUser.getId())) {
            throw new InvalidAuthorityException("Workers have no authorization to create/update/delete any other user.");
        }
    }

}
