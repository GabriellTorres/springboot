package com.aprendendo.login.service;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aprendendo.login.dto.AuthDto;
import com.aprendendo.login.entity.Role;
import com.aprendendo.login.entity.User;
import com.aprendendo.login.repository.RoleRepository;
import com.aprendendo.login.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User registerPublico(AuthDto dto) {

        Role roleDefault = roleRepository.findByName(Role.Values.ROLE_BASIC.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role BASICA não encontrada"));

        if (userRepository.findByUsername(dto.username()) != null) {
            throw new IllegalArgumentException("Username já foi cadastrado");
        }

        var passwordHash = passwordEncoder.encode(dto.password());

        User user = new User(dto.username(), passwordHash);
        user.setRoles(Set.of(roleDefault));

        userRepository.save(user);
        return user;
    }

    public User registerPrivate(AuthDto dto) {

        /*
         * Set<Role> roles = dto.roleNames().stream().map(name ->
         * roleRepository.findByName(name)
         * .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " +
         * name)))
         * .collect(Collectors.toSet());
         */

        Role roleAdmin;
        Role roleBasic;

        roleAdmin = roleRepository.findByName(Role.Values.ROLE_ADMIN.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role ADMIN não encontrada"));

        roleBasic = roleRepository.findByName(Role.Values.ROLE_BASIC.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role BASIC não encontrada"));

        Set<Role> roles = Set.of(roleAdmin, roleBasic);

        var passwordHash = passwordEncoder.encode(dto.password());

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordHash);
        user.setRoles(roles);

        userRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return user;
    }

}
