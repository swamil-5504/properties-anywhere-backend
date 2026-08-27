package com.propertiesanywhere.properties_anywhere_backend.service;

import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import com.propertiesanywhere.properties_anywhere_backend.model.User;
import com.propertiesanywhere.properties_anywhere_backend.repository.PropertyRepository;
import com.propertiesanywhere.properties_anywhere_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyService(
            PropertyRepository propertyRepository,
            UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    public List<Property> searchByCity(String city) {
        return propertyRepository.findByCityIgnoreCase(city);
    }

    public Property createProperty(Property property, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        property.setUser(user);

        return propertyRepository.save(property);
    }

    public List<Property> getPropertiesByUser(Long userId) {
        return propertyRepository.findByUserId(userId);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }
}