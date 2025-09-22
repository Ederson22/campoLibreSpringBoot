// Archivo: src/main/java/com/proyecto/campoLibre/config/SecurityConfig.java

package com.proyecto.campoLibre.config;

import com.proyecto.campoLibre.implement.UsuarioServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioServiceImpl usuarioServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationSuccessHandler loginSuccessHandler; // <-- ¡Declaramos la variable aquí!

    public SecurityConfig(UsuarioServiceImpl usuarioServiceImpl, PasswordEncoder passwordEncoder, AuthenticationSuccessHandler loginSuccessHandler) {
        this.usuarioServiceImpl = usuarioServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.loginSuccessHandler = loginSuccessHandler; // <-- ¡La asignamos en el constructor!
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/proveedor/**").hasAnyRole("PROVEEDOR", "ADMINISTRADOR")
                        .requestMatchers("/consumidor/**").hasAnyRole("CONSUMIDOR", "ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("contrasena")
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}