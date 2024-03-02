package org.example.userservice.Services;


import org.example.userservice.Models.User;
import org.example.userservice.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signUp(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) {
          User user = new User();
          user.setEmail(email);
          user.setPassword(bCryptPasswordEncoder.encode(password));
          User saveduser = userRepository.save(user);
          return saveduser;
        }

        return userOptional.get();
    }

    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) {
          return null;
        }

        User user = userOptional.get();

        //if(!user.getPassword().equals(password)) {
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())) {
            return null;
        }

        return user;

    }
}
