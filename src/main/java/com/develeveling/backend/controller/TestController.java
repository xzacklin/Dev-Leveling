package com.develeveling.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test") // All endpoints in this class will start with this path
public class TestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Chupapi Munyanyo";
    }
}