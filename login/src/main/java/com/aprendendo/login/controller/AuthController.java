package com.aprendendo.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendendo.login.dto.AuthDto;
import com.aprendendo.login.dto.LoginResponseDto;
import com.aprendendo.login.entity.User;
import com.aprendendo.login.service.AuthService;
import com.aprendendo.login.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, TokenService tokenService){
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AuthDto dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }
	
    @PostMapping("/registerUser")
    public ResponseEntity<Void> registrarUsuario(@RequestBody AuthDto dto){
        authService.registerPublico(dto);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registerAdmin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> registrarAdministrador(@RequestBody AuthDto dto){
        authService.registerPrivate(dto);

        return ResponseEntity.ok().build();
    }
}
