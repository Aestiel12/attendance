package com.aestiel.attendance.services.implementations;

import com.aestiel.attendance.repositories.UserRepository;
import com.aestiel.attendance.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
