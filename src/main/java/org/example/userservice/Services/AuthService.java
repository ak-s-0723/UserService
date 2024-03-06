package org.example.userservice.Services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userservice.Models.Session;
import org.example.userservice.Models.SessionStatus;
import org.example.userservice.Models.User;
import org.example.userservice.Repositories.SessionRepository;
import org.example.userservice.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

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

    public Pair<User,MultiValueMap<String,String>> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
          return null;
        }
        User user = userOptional.get();
        //if(!user.getPassword().equals(password)) {
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())) {
            return null;
        }


        //Token Generation
//        String message = "{\n" +
//                "   \"email\": \"anurag@scaler.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2024\"\n" +
//                "}";
//
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

        Map<String,Object> jwtData = new HashMap<>();
        jwtData.put("email",user.getEmail());
        jwtData.put("roles",user.getRoles());
        long nowInMillis = System.currentTimeMillis();
        jwtData.put("expiryTime",new Date(nowInMillis+10000));
        jwtData.put("createdAt",new Date(nowInMillis));


        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secret = algorithm.key().build();
        String token = Jwts.builder().claims(jwtData).signWith(secret).compact();

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        session.setExpiringAt(new Date(nowInMillis+10000));
        sessionRepository.save(session);

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);
        return new Pair<User,MultiValueMap<String,String>>(user,headers);
    }
}
