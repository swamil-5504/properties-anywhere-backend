package com.propertiesanywhere.properties_anywhere_backend.controller;

import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import com.propertiesanywhere.properties_anywhere_backend.service.PropertyService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = "multipart/form-data")
    public Property createProperty(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam Double rent,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image) {

        return propertyService.createProperty(
                title,
                city,
                address,
                rent,
                description,
                image,
                userId
        );
    }

    @GetMapping("/user/{userId}")
    public List<Property> getPropertiesByUser(
            @PathVariable Long userId) {

        return propertyService.getPropertiesByUser(userId);
    }

    @PutMapping(
            value = "/{id}",
            consumes = "multipart/form-data"
    )
    public Property updateProperty(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam Double rent,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image) {

        return propertyService.updateProperty(
                id,
                userId,
                title,
                city,
                address,
                rent,
                description,
                image
        );
    }
}