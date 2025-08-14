package com.aprendendo.introducao_testes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    List<User> users = new ArrayList<>();
    
    public List<User>  create(User user){

        if(users.stream().anyMatch(u -> u.username().equals(user.username()))) {
            throw new RuntimeException("Username must be unique");
        }

        users.add(user);
        return users;
    }
}
