package com.dacm.taskManager.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // Initialize JdbcUserDetailsManager with the provided DataSource
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // Query to retrieve user information by username
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, pw, 1 as active FROM usuarios WHERE username=?");
        // Query to retrieve user roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select roles.user_id, roles.rol from roles inner join gestor_tasks.usuarios u on roles.user_id = u.user_id where username=?");

        return jdbcUserDetailsManager;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET,"api/v1/users/users").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.GET,"api/v1/users/allUsers").hasAnyRole("USER", "ADMIN")
        );

        // Use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // Disable Cross Site Request Forgery (CSRF)
        // In general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Utiliza un PasswordEncoder estándar sin prefijo
        return new BCryptPasswordEncoder();
    }



}













