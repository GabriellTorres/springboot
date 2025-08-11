package com.aprendendo.login.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aprendendo.login.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = recoverToken(request);

        if(token != null){
            var decodedJWT = tokenService.verifyToken(token);
            if(decodedJWT != null) {
                var subject = decodedJWT.getSubject();
                var roles = decodedJWT.getClaim("roles").asList(String.class);

                if(subject != null && roles != null) {
                    var authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    
                    var authentication = new UsernamePasswordAuthenticationToken(
                            subject, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return null;

        return authHeader.replace("Bearer ", "").trim(); // remove "Bearer " e espaços
    }

}
