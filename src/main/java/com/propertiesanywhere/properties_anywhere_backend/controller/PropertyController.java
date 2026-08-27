package com.propertiesanywhere.properties_anywhere_backend.controller;

import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import com.propertiesanywhere.properties_anywhere_backend.service.PropertyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<Property> searchProperties(
            @RequestParam String city) {

        return propertyService.searchByCity(city);
    }

    @GetMapping("/{id}")
    public Property getProperty(
            @PathVariable Long id) {

        return propertyService.getPropertyById(id);
    }

    @PostMapping
    public Property createProperty(
            @RequestParam Long userId,
            @RequestBody Property property) {

        return propertyService.createProperty(
                property,
                userId
        );
    }

    @GetMapping("/user/{userId}")
    public List<Property> getPropertiesByUser(
            @PathVariable Long userId) {

        return propertyService.getPropertiesByUser(userId);
    }

    @PutMapping("/{id}")
    public Property updateProperty(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody Property property) {

        return propertyService.updateProperty(
                id,
                userId,
                property
        );
    }
}

