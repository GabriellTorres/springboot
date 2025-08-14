package com.aprendendo.introducao_testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;


public class UserServiceTest {
    
    private UserService userService = new UserService();

    @Test
    public void testCreateUser(){

        // Arrange
        // Criando um usuário de teste
        User user = new User("testUser", "testPassword");
        
        // Act
        // Chamando o método create do UserService
        List<User> users = userService.create(user);

        // JUnit

        // Assert
        // Verificando se o usuário foi adicionado corretamente
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).username());
        assertEquals("testPassword", users.get(0).password());
        assertEquals(user, users.get(0));
    }

    @Test
    public void TestCreateUser_ValueUnique(){
        
        User user1 = new User("uniqueUser", "uniquePassword");
        User user2 = new User("uniqueUser", "otherPassword");

        userService.create(user1);

        assertThrows(Exception.class, () -> userService.create(user2));
    }   
}   
