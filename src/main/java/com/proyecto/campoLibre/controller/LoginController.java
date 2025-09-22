// Archivo: src/main/java/com/proyecto/campoLibre/controller/LoginController.java

package com.proyecto.campoLibre.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login/login"; // Busca la vista en src/main/resources/templates/login/login.html
    }
}