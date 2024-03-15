package org.example.userservice.Security.Services;

import org.example.userservice.Models.User;
import org.example.userservice.Repositories.UserRepository;
import org.example.userservice.Security.Models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
//username = ly , password = ly which can be tried for spring security
@Service
public class CustomUserDetailsService implements UserDetailsService  {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Not Found");
        }

        User user = optionalUser.get();
        return new CustomUserDetails(user);
    }
}
