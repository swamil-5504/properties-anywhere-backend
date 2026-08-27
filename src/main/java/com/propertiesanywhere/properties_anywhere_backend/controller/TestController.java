package com.propertiesanywhere.properties_anywhere_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test(){
        return "Properties Anywhere backend is working";
    }
}
