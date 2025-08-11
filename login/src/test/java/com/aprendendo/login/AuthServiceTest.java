/*package com.aprendendo.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.aprendendo.login.dto.RegisterDto;
import com.aprendendo.login.entity.Role;
import com.aprendendo.login.entity.User;
import com.aprendendo.login.repository.UserRepository;
import com.aprendendo.login.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Lançar exceção se username já cadastrado")
    public void testRegisterUsernameJaCadastrado(){

        RegisterDto dto = new RegisterDto("usuario", "123");

        when(userRepository.findByUsername("usuario")).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.register(dto));

        assertEquals("Username já foi cadastrado", exception.getMessage());
    }
    
}
*/