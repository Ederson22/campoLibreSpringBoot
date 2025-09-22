// Archivo: src/main/java/com/proyecto/campoLibre/config/LoginSuccessHandler.java

package com.proyecto.campoLibre.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/"; // URL por defecto si el rol no coincide

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMINISTRADOR")) {
                redirectUrl = "/admin";
                break;
            } else if (role.equals("ROLE_PROVEEDOR")) {
                redirectUrl = "/proveedor";
                break;
            } else if (role.equals("ROLE_CONSUMIDOR")) {
                redirectUrl = "/consumidor";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}