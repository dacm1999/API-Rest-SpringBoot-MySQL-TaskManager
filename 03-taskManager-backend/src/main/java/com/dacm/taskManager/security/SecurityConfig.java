package com.dacm.taskManager.security;

import com.dacm.taskManager.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                                authRequest
                                        // Allow all requests to the following endpoints
                                        // Authentication endpoint (login)
                                        .requestMatchers("v1/auth/**").permitAll()

                                        // Password endpoint
                                        .requestMatchers("v1/password/**").permitAll()

                                        // Priorities endpoint
                                        .requestMatchers("v1/priorities/{name}").permitAll()
                                        .requestMatchers("v1/priorities/info/{id}").permitAll()
                                        .requestMatchers("v1/priorities/all").permitAll()
                                        .requestMatchers("v1/priorities/update/{id}").hasRole("ADMIN")
                                        .requestMatchers("v1/priorities/delete/{id}").hasRole("ADMIN")
                                        .requestMatchers("v1/priorities/create").hasRole("ADMIN")
                                        .requestMatchers("v1/priorities/createMany").hasRole("ADMIN")

                                        // Tags endpoint
                                        .requestMatchers("v1/tags/create").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tags/createMany").hasRole("ADMIN")
                                        .requestMatchers("v1/tags/info/name/{tagName}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tags/info/id/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tags/allTags").hasRole("ADMIN")
                                        .requestMatchers("v1/tags/update/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tags/delete/{id}").hasAnyRole("ADMIN", "USER")

                                        // Tasks endpoint
                                        .requestMatchers("v1/tasks/create").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tasks/createMany").hasAnyRole("ADMIN")
                                        .requestMatchers("v1/tasks/info/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tasks/user/{username}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tasks/all").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tasks/usertasks/{username}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tasks/update/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/tasks/delete/{id}").hasAnyRole("ADMIN", "USER")

                                        //Users endpoint
                                        .requestMatchers("v1/users/createMultiple").hasRole("ADMIN")
                                        .requestMatchers("v1/users/info-id/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/users/info-username/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/users/allUsers").hasRole("ADMIN")
                                        .requestMatchers("v1/users/update/{id}").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("v1/users/delete/{id}").hasAnyRole("ADMIN", "USER")

                                        //Swagger endpoint
                                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
                                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager -> sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
