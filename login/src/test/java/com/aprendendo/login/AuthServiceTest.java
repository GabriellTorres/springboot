package com.aprendendo.login;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.aprendendo.login.dto.AuthDto;
import com.aprendendo.login.entity.Role;
import com.aprendendo.login.entity.User;
import com.aprendendo.login.repository.RoleRepository;
import com.aprendendo.login.repository.UserRepository;
import com.aprendendo.login.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;
    @Nested
    @DisplayName("Testes de registro de usuário")
    class RegisterUsersTest{

        @Test
        public void deveRegistrarUsuarioComSucesso(){

            AuthDto dto = new AuthDto("testuser", "password123");

            String senhaHash = "$2a$10$hashFalsoParaTeste";

            String roleDefault = Role.Values.ROLE_BASIC.name();

            Role role = new Role();
            role.setName(roleDefault);
            role.setId(1L);

            when(userRepository.findByUsername(dto.username())).thenReturn(null);
            when(roleRepository.findByName(roleDefault)).thenReturn(Optional.of(role));
            when(passwordEncoder.encode(dto.password())).thenReturn(senhaHash);
            
            User user = authService.registerPublico(dto);

            assertNotNull(user);
            assertEquals("testuser",user.getUsername());
            assertEquals(senhaHash,user.getPassword());
            assertEquals(1,user.getRoles().size());
            verify(userRepository, times(1)).save(any(User.class));
            
        }

        @Test
        public void deveregistrarAdminComSucesso(){
            AuthDto dto = new AuthDto("adminuser", "adminpassword");
            String senhaHash = "$2a$10$hashFalsoParaTeste";
            String role1 = Role.Values.ROLE_ADMIN.name();
            String role2 = Role.Values.ROLE_BASIC.name();

            Role roleAdmin = new Role();
            roleAdmin.setName(role1);
            roleAdmin.setId(1L);

            Role roleBasic = new Role();
            roleBasic.setName(role2);
            roleBasic.setId(2L);

            when(roleRepository.findByName(role1)).thenReturn(Optional.of(roleAdmin));
            when(roleRepository.findByName(role2)).thenReturn(Optional.of(roleBasic));
            when(passwordEncoder.encode(dto.password())).thenReturn(senhaHash);

            User user = authService.registerPrivate(dto);

            assertNotNull(user);
            assertEquals("adminuser", user.getUsername());
            assertEquals(senhaHash, user.getPassword());
            assertEquals(2, user.getRoles().size());
            verify(userRepository, times(1)).save(any(User.class));


        }

        @Test
        public void deveLancarExcecaoQuandoUsernameJaExistir(){

            AuthDto dto = new AuthDto("existinguser", "password123");

            String senhaHash = "$2a$10$hashFalsoParaTeste";

            String roleDefault = Role.Values.ROLE_BASIC.name();

            Role role = new Role();
            role.setName(roleDefault);
            role.setId(1L);

            User existingUser = new User();
            existingUser.setId(1L);
            existingUser.setUsername("existinguser");
            existingUser.setPassword(senhaHash);

            when(userRepository.findByUsername(dto.username())).thenReturn(existingUser);
            when(roleRepository.findByName(roleDefault)).thenReturn(Optional.of(role));
            //when(passwordEncoder.encode(dto.password())).thenReturn(senhaHash);

            assertThrows(IllegalArgumentException.class, () -> {
                authService.registerPublico(dto);
            });
            verify(userRepository, times(0)).save(any(User.class));

        }

    }

    @Nested
    @DisplayName("Testes de login de usuário")
    class loginUsersTest{

        @Test
        public void deveLogarQuandoUsernameExistir(){

            AuthDto dto = new AuthDto("testuser", "password123");

            Role role = new Role();
            role.setName(Role.Values.ROLE_BASIC.name());

            User user = new User();
            user.setUsername(dto.username());
            user.setPassword("$2a$10$hashFalsoParaTeste");
            user.setId(1L);
            user.setRoles(Set.of(role));

            when(userRepository.findByUsername(dto.username())).thenReturn(user);

            UserDetails userDetails = authService.loadUserByUsername(dto.username());
            verify(userRepository, times(1)).findByUsername(dto.username());
            assertNotNull(userDetails);
            assertEquals(user.getUsername(), userDetails.getUsername());
            assertEquals(user.getPassword(), userDetails.getPassword());
            assertEquals(user.getRoles().size(), userDetails.getAuthorities().size());

        }

        @Test
        public void deveLancarExcecaoQuandoUsernameJaExistir(){

            AuthDto dto = new AuthDto("testuser", "password123");

            Role role = new Role();
            role.setName(Role.Values.ROLE_BASIC.name());

            User user = new User();
            user.setUsername(dto.username());
            user.setPassword("$2a$10$hashFalsoParaTeste");
            user.setId(1L);
            user.setRoles(Set.of(role));

            when(userRepository.findByUsername(dto.username())).thenReturn(null);

            assertThrows(UsernameNotFoundException.class, () -> {
                authService.loadUserByUsername(dto.username());
            });
        }
    }
    
}
