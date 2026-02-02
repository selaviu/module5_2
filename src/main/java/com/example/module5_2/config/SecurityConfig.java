package com.example.module5_2.config;

import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${FRONTEND_URL}")
    private String frontedUrl;
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/",
                "/index.html", 
                "/static/**",       
                "/*.js",             
                "/*.json",           
                "/*.png",           
                "/login**", "/error", "/health").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage(frontedUrl + "/login?lang=ua")
                
            .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler(frontedUrl + "/songsList?lang=ua"))
        ).logout(logout -> logout
            .logoutUrl("/logout") 
            .logoutSuccessHandler(logoutSuccessHandler())
        );

        return http.build();
    }

    private ServerLogoutSuccessHandler logoutSuccessHandler() {
    RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
    logoutSuccessHandler.setLogoutSuccessUrl(URI.create(frontedUrl + "/login?lang=ua"));
    return logoutSuccessHandler;
}
    @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true); 

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}