// Archivo: src/main/java/com/proyecto/campoLibre/controller/AdminController.java

package com.proyecto.campoLibre.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String adminHome() {
        return "admin/adminHome"; // Ruta a la vista: templates/admin/dashboard.html
    }

    @GetMapping("/users")
    public String manageUsers() {
        return "admin/users"; // Ruta a la vista: templates/admin/users.html
    }
}