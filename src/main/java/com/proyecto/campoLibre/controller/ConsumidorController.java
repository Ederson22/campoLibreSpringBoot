// Archivo: src/main/java/com/proyecto/campoLibre/controller/ConsumidorController.java

package com.proyecto.campoLibre.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/consumidor")
public class ConsumidorController {

    @GetMapping
    public String consumidorHome() {
        return "consumidor/consumidorHome"; // Ruta a la vista: templates/consumidor/dashboard.html
    }

    @GetMapping("/events")
    public String viewEvents() {
        return "consumidor/events"; // Ruta a la vista: templates/consumidor/events.html
    }
}