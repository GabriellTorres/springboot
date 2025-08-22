package com.aprendendo.encurtadorurl.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aprendendo.encurtadorurl.dto.ShortenUrlRequest;
import com.aprendendo.encurtadorurl.dto.ShortenUrlResponse;
import com.aprendendo.encurtadorurl.entity.Url;
import com.aprendendo.encurtadorurl.repository.UrlRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

@RestController
public class UrlController {

    private final UrlRepository urlRepository;
    private static final String alphaum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping(value = "/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request, HttpServletRequest httpServletRequest) {

        String id;
        int tamanho = 8;

        do{
            StringBuilder sb = new StringBuilder(tamanho);

            for (int i = 0; i < tamanho; i++) {
                sb.append(alphaum.charAt(random.nextInt(alphaum.length()))); //gerando um ID aleatório para a URL
            }
            id = sb.toString();
        }while (urlRepository.existsById(id)); 
        
        urlRepository.save(new Url(id, request.url(), LocalDateTime.now().plusMinutes(1)));

        var redirectUrl = httpServletRequest.getRequestURL().toString().replace("shorten-url", id); //substituindo a parte do URL pelo ID gerado

        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl)); //retornando a URL modificada
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id){

        var url = urlRepository.findById(id);

        if (url.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //retorna 404 se a URL não for encontrada
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(java.net.URI.create(url.get().getOriginalUrl())); //criando o cabeçalho de redirecionamento

        return ResponseEntity.status(HttpStatus.FOUND) //status 302 para redirecionamento
                .headers(headers)
                .build(); //retornando a resposta com o cabeçalho de redirecionamento
        
        
    }
}
