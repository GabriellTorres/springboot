package com.aprendendo.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aprendendo.login.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUser(Long id) {
        if (!userExists(id)) {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
