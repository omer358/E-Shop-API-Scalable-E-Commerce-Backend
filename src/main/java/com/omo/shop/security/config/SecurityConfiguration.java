package com.omo.shop.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(apiPrefix + "/auth/**").permitAll()
                                .requestMatchers(apiPrefix + "/users/create-user").permitAll()
                                // 🔒 SECURE WRITE OPERATIONS FOR PRODUCTS
                                .requestMatchers(HttpMethod.POST, apiPrefix + "/products/**")
                                .hasRole("ADMIN") // /add
                                .requestMatchers(HttpMethod.PUT, apiPrefix + "/products/**")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, apiPrefix + "/products/**")
                                .hasRole("ADMIN")

                                // 🔒 SECURE WRITE OPERATIONS FOR IMAGES
                                .requestMatchers(HttpMethod.POST, apiPrefix + "/images/upload")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, apiPrefix + "/images/**")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, apiPrefix + "/images/**")
                                .hasRole("ADMIN")

                                // 🔓 ALLOW READ-ONLY GET OPERATIONS FOR PRODUCTS
                                .requestMatchers(HttpMethod.GET, apiPrefix + "/products/**")
                                .permitAll()

                                // 🔓 ALLOW READ-ONLY GET OPERATIONS FOR IMAGES
                                .requestMatchers(HttpMethod.GET, apiPrefix + "/images/**")
                                .permitAll()

                                .anyRequest().authenticated()
                )
                .sessionManagement(mange ->
                        mange.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

