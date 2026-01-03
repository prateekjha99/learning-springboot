package com.learning.springboot.secondapp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Value("${person.name}")
    private String personName;

    @Value("${city.name}")
    private String cityName;

    @GetMapping("/")
    public String hello() {
        return "Person: " + personName + " . City: " + cityName;
    }
}
