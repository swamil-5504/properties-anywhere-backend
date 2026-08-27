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
    public List<Property> getProperties(@RequestParam(required = false) String city){
        return propertyService.getPropertiesByCity(city);
    }

    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyService.createProperty(property);
    }

}
