package com.brunoams.accesscontrol.repository;

import com.brunoams.accesscontrol.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
