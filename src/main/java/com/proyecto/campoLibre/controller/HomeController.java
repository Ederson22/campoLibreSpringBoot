// Archivo: src/main/java/com/proyecto/campoLibre/controller/HomeController.java

package com.proyecto.campoLibre.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home/index"; // Retorna la vista en templates/home/index.html
    }
}