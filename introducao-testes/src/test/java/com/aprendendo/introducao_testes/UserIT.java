package com.aprendendo.introducao_testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testCreateUser() {
         User user = new User("testUser", "testPassword");

        User[] users = restTemplate.postForObject("/users", user, User[].class);

        assertNotNull(users);
        assertEquals(1, users.length);
        assertEquals("testUser", users[0].username());
        assertEquals("testPassword", users[0].password());
        assertThrows(RuntimeException.class, () -> {
            restTemplate.postForObject("/users", user, User[].class);
        });
    }
}
