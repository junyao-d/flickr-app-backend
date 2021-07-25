package com.gcp.spring.mysql.service;

import com.gcp.spring.mysql.model.User;  
import com.gcp.spring.mysql.repository.UserRepository;  

import java.util.ArrayList;  
import java.util.List;  
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findById(username).get();
    }

    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public String generateToken(String username, String password) {
        String timestamp = Long.toString(System.currentTimeMillis());
        int hash = username.hashCode() * password.hashCode() * timestamp.hashCode();
        String token = Integer.toString(hash);
        return token;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public boolean userExist(String username){
        boolean exist = userRepository.existsById(username);
        return exist;
    }

}