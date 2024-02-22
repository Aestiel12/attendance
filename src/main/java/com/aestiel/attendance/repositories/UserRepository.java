package com.aestiel.attendance.repositories;

import com.aestiel.attendance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserById(Long id);
    boolean existsByEmail(String email);
}
