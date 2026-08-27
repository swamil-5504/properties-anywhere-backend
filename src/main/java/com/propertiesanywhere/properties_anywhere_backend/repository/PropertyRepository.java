package com.propertiesanywhere.properties_anywhere_backend.repository;

import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByCityIgnoreCase(String city);

    List<Property> findByUserId(Long userId);
}