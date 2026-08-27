package com.propertiesanywhere.properties_anywhere_backend.repository;

import com.propertiesanywhere.properties_anywhere_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}