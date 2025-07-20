package com.stratumtech.realtyticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.stratumtech.realtyticket.config.filter.FindUserCredentialsFilter;

@Configuration
public class SecurityConfig {
    private final FindUserCredentialsFilter findUserCredentialsFilter;

    public SecurityConfig(FindUserCredentialsFilter findUserCredentialsFilter) {
        this.findUserCredentialsFilter = findUserCredentialsFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(findUserCredentialsFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}