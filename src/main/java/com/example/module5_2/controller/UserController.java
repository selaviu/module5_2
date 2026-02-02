package com.example.module5_2.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @GetMapping("/profile")
    public Mono<ResponseEntity<Map<String, String>>> getUserProfile(@AuthenticationPrincipal OidcUser principal) {
        
        if (principal == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        String name = principal.getFullName(); 
        String email = principal.getEmail();
        
        return Mono.just(ResponseEntity.ok(Map.of(
            "name", name, 
            "email", email
        )));
    }
}
