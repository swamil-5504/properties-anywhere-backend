package com.propertiesanywhere.properties_anywhere_backend.service;

import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import com.propertiesanywhere.properties_anywhere_backend.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> getPropertiesByCity(String city){
        if(city == null || city.isBlank()){
            return propertyRepository.findAll();
        }
        return propertyRepository.findByCityIgnoreCase(city);
    }

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

}
