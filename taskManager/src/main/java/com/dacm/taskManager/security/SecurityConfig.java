package com.dacm.taskManager.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Consulta para obtener la información del usuario por nombre de usuario
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, pw, 1 as active FROM usuarios WHERE username=?");

        // Consulta para obtener los roles del usuario por nombre de usuario
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id, rol FROM roles WHERE user_id=(SELECT user_id FROM usuarios WHERE username=?)");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select roles.user_id, roles.rol from roles inner join gestor_tasks.usuarios u on roles.user_id = u.user_id where username=?");

        return jdbcUserDetailsManager;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/test").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/test").hasRole("ADMIN")
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }


}













