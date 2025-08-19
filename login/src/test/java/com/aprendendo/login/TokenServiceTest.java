package com.aprendendo.login;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.aprendendo.login.entity.Role;
import com.aprendendo.login.entity.User;
import com.aprendendo.login.service.TokenService;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private Algorithm algorithm;

    @InjectMocks
    private TokenService tokenService;

    @Nested
    @DisplayName("Testes de geração de token")
    class GenerateTokenTests {
        
        @Test
        public void deveGerarTokenComSucesso() {

            Role roleBasic = new Role();
            roleBasic.setName(Role.Values.ROLE_BASIC.name());
            roleBasic.setId(1L);
            
            User user = new User();
            user.setId(1L);
            user.setUsername("testuser");
            user.setPassword("password123");
            user.setRoles(Set.of(roleBasic));

            String secret = "mySecretKey";

            // injeta manualmente o valor do @Value
            ReflectionTestUtils.setField(tokenService, "secret", secret);

            String secretKeyHash = "hashedSecretKey";
            
            Algorithm mockAlgorithm = mock(Algorithm.class);
           

            try (MockedStatic<Algorithm> mockedAlgorithm = Mockito.mockStatic(Algorithm.class)) {
                mockedAlgorithm.when(() -> Algorithm.HMAC256(secret))
                       .thenReturn(mockAlgorithm);
            }

            String token = tokenService.generateToken(user);
            assertNotNull(token);
            
        }   
    }

}
