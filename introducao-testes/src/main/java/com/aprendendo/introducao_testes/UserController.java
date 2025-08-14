package com.aprendendo.introducao_testes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/users")
    public List<User> createUser(@RequestBody User user) {
        return userService.create(user);
    }
}
