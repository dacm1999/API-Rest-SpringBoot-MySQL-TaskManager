package com.dacm.taskManager.security;


//@Configuration
//public class SecurityConfig {
//
//
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        // Initialize JdbcUserDetailsManager with the provided DataSource
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        // Query to retrieve user information by username
//        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, pw, 1 as active FROM usuarios WHERE username=?");
//        // Query to retrieve user roles by username
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select roles.user_id, roles.rol from roles inner join gestor_tasks.usuarios u on roles.user_id = u.user_id where username=?");
//
//        return jdbcUserDetailsManager;
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//
//        http
//        .csrf(csrf ->
//                csrf.disable())
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/auth/login").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults()) // Configurar autenticación básica
//                .formLogin(withDefaults()); // Configurar autenticación basada en formulario
//
////
////        http.authorizeHttpRequests(configurer -> configurer
////                .requestMatchers(HttpMethod.POST,"api/v1/users/").hasRole("ADMIN")
////                .requestMatchers(HttpMethod.GET,"api/v1/users/users").hasAnyRole("USER","ADMIN")
////                .requestMatchers(HttpMethod.GET,"api/v1/users/allUsers").hasAnyRole("USER", "ADMIN")
////        );
//
//        // Use HTTP Basic authentication
//        http.httpBasic(withDefaults());
//
//        // Disable Cross Site Request Forgery (CSRF)
//        // In general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
//        http.csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // Utiliza un PasswordEncoder estándar sin prefijo
//        return new BCryptPasswordEncoder();
//    }
//
//
//
//}













