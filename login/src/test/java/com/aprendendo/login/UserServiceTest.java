package com.aprendendo.login;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aprendendo.login.entity.User;
import com.aprendendo.login.repository.UserRepository;
import com.aprendendo.login.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class DeleteUserTests {
        @Test
        public void deveDeletarUsuarioExistente(){

            //Arrage
            Long userId = 1L;
            User user = new User();
            user.setId(userId);
            
            // Act
            when(userRepository.existsById(userId)).thenReturn(true);
            userService.deleteUser(userId);

            // Assert
            verify(userRepository, times(1)).deleteById(userId); 

        }

        @Test
        public void deveLançarExcecaoQuandoUsuarioNaoExistir() {

            Long userId = 5L;

            when(userRepository.existsById(userId)).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> {
                userService.deleteUser(userId);
            });
        }
    }
}
