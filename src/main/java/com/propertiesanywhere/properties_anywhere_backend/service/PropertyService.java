package com.propertiesanywhere.properties_anywhere_backend.service;

import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import com.propertiesanywhere.properties_anywhere_backend.model.User;
import com.propertiesanywhere.properties_anywhere_backend.repository.PropertyRepository;
import com.propertiesanywhere.properties_anywhere_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    private final Path uploadDirectory =
            Paths.get("uploads").toAbsolutePath().normalize();

    public PropertyService(
            PropertyRepository propertyRepository,
            UserRepository userRepository) {

        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory",
                    e
            );
        }
    }

    public List<Property> searchByCity(String city) {
        return propertyRepository.findByCityIgnoreCase(city);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));
    }

    public Property createProperty(
            String title,
            String city,
            String address,
            Double rent,
            String description,
            MultipartFile image,
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Property property = new Property();

        property.setTitle(title);
        property.setCity(city);
        property.setAddress(address);
        property.setRent(rent);
        property.setDescription(description);
        property.setUser(user);

        if (image != null && !image.isEmpty()) {
            property.setImageUrl(saveImage(image));
        }

        return propertyRepository.save(property);
    }

    public List<Property> getPropertiesByUser(Long userId) {
        return propertyRepository.findByUserId(userId);
    }

    public Property updateProperty(
            Long propertyId,
            Long userId,
            String title,
            String city,
            String address,
            Double rent,
            String description,
            MultipartFile image) {

        Property existingProperty = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        /*
         * Make sure only the owner can edit the property.
         */
        if (existingProperty.getUser() == null ||
                !existingProperty.getUser().getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to update this property");
        }

        existingProperty.setTitle(title);
        existingProperty.setCity(city);
        existingProperty.setAddress(address);
        existingProperty.setRent(rent);
        existingProperty.setDescription(description);

        /*
         * Only replace the image if the user selected
         * a new image.
         *
         * If no image was uploaded, the existing image
         * remains unchanged.
         */
        if (image != null && !image.isEmpty()) {

            String oldImageUrl = existingProperty.getImageUrl();

            String newImageUrl = saveImage(image);

            existingProperty.setImageUrl(newImageUrl);

            /*
             * Delete the old image after the new image
             * has been successfully saved.
             */
            deleteImage(oldImageUrl);
        }

        return propertyRepository.save(existingProperty);
    }

    /**
     * Saves an uploaded image to the uploads directory.
     *
     * Returns a path such as:
     *
     * /uploads/550e8400-e29b-41d4-a716-446655440000.jpg
     */
    private String saveImage(MultipartFile image) {

        if (image == null || image.isEmpty()) {
            return null;
        }

        String originalFilename = image.getOriginalFilename();

        /*
         * Determine the file extension from the original filename.
         */
        String extension = ".jpg";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension = originalFilename
                    .substring(originalFilename.lastIndexOf("."))
                    .toLowerCase();
        }

        /*
         * Normalize some extensions that may come from
         * phones/cameras.
         */
        if (extension.equals(".jpeg")) {
            extension = ".jpg";
        }

        /*
         * Only allow common image file extensions.
         */
        if (!extension.equals(".jpg")
                && !extension.equals(".png")
                && !extension.equals(".webp")
                && !extension.equals(".gif")) {

            throw new RuntimeException(
                    "Only JPG, PNG, WEBP and GIF images are allowed"
            );
        }

        /*
         * Generate a unique filename.
         */
        String filename =
                UUID.randomUUID().toString() + extension;

        Path targetPath =
                uploadDirectory
                        .resolve(filename)
                        .normalize();

        /*
         * Security check: make sure the file cannot
         * escape the uploads directory.
         */
        if (!targetPath.startsWith(uploadDirectory)) {
            throw new RuntimeException(
                    "Invalid file path"
            );
        }

        try (InputStream inputStream = image.getInputStream()) {

            Files.copy(
                    inputStream,
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not save image",
                    e
            );
        }

        return "/uploads/" + filename;
    }

    /**
     * Deletes an old property image.
     */
    private void deleteImage(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        /*
         * We only delete files belonging to our
         * local /uploads directory.
         */
        if (!imageUrl.startsWith("/uploads/")) {
            return;
        }

        String filename =
                imageUrl.substring("/uploads/".length());

        Path imagePath =
                uploadDirectory.resolve(filename)
                        .normalize();

        if (!imagePath.startsWith(uploadDirectory)) {
            return;
        }

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            /*
             * Do not fail the whole property update
             * just because deleting an old image failed.
             */
            System.err.println(
                    "Could not delete old image: "
                            + imagePath
            );
        }
    }
}