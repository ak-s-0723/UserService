package org.example.userservice.Services;

import org.example.userservice.Models.User;
import org.example.userservice.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserDetails(Long id) {
        return userRepository.findById(id).get();
    }
}
