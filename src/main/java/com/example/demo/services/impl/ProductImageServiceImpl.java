package com.example.demo.services.impl;

import com.example.demo.dto.ProductImageDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductImage;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.services.ProductImageService;
import com.example.demo.services.mappers.ProductImageServiceMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;

    private static final String IMAGE_FOLDER_PATH = "src/main/resources/static/images/product/";

    public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }


    @Override
    public List<ProductImageDTO> findAllProductImages() {
        List<ProductImage> productImageDTOS = productImageRepository.findAll();
        return productImageDTOS.stream()
                .map(ProductImageServiceMapper::toProductImageDTO)
                .collect(Collectors.toList());

    }

    @Override
    public ProductImageDTO findProductImageById(Long id) {
        return productImageRepository.findById(id)
                .map(ProductImageServiceMapper::toProductImageDTO)
                .orElse(null);
    }

    @Override
    public Resource getImageResponse(String imagePath) {
        String filePath = IMAGE_FOLDER_PATH + imagePath;
        Resource resource = new FileSystemResource(filePath);
        if (resource.exists()) {
            return resource;
        } else {
            return null;
        }
    }


    @Override
    public ResponseEntity<?> createProductImage(Long productId, MultipartFile imageFile) {
        try {

            File directory = new File(IMAGE_FOLDER_PATH);
            if (!directory.exists()) {
                directory.mkdirs(); // create the directory and any necessary parent directories
            }


            // Upload the image file to Google Drive
            byte[] imageBytes = imageFile.getBytes();
            // Store the image in a resources folder

            ByteArrayInputStream inStreambj = new ByteArrayInputStream(imageBytes);

            // read image from byte array
            BufferedImage newImage = ImageIO.read(inStreambj);

            // write output image
            ImageIO.write(newImage, "jpg", new File(getImageUrl(imageFile.getOriginalFilename())));


            // Save product image details in the database
            ProductImage productImage = ProductImage.builder()
                    .imagePath(imageFile.getOriginalFilename())
                    .product(Product.builder().id(productId).build())
                    .build();
            productImageRepository.save(productImage);

            return ResponseEntity.ok(ProductImageServiceMapper.toProductImageDTO(productImage));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateProductImage(Long id, Long productId, MultipartFile imageFile) {
        ProductImage productImage = productImageRepository.findById(id).orElse(null);
        if (productImage == null) {
            return ResponseEntity.badRequest().body("Product image not found");
        }
        if (productImageRepository.existsByProductIdAndImagePath(productId, imageFile.getOriginalFilename())) {
            return ResponseEntity.badRequest().body("Product id and url already exist");
        }

        try {
            // Upload the image file to Google Drive
            byte[] imageBytes = imageFile.getBytes();
            // Store the image in a resources folder

            ByteArrayInputStream inStreambj = new ByteArrayInputStream(imageBytes);

            // read image from byte array
            BufferedImage newImage = ImageIO.read(inStreambj);

            // write output image
            ImageIO.write(newImage, "jpg", new File(getImageUrl(imageFile.getOriginalFilename())));

            // Delete the old image file
            File oldImageFile = new File(IMAGE_FOLDER_PATH + productImage.getImagePath());
            oldImageFile.delete();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        }
        productImage.setImagePath(imageFile.getOriginalFilename());
        productImage.setProduct(Product.builder().id(productId).build());
        productImageRepository.save(productImage);
        return ResponseEntity.ok(ProductImageServiceMapper.toProductImageDTO(productImage));
    }

    @Override
    public ResponseEntity<?> deleteProductImage(Long id) {
        ProductImage productImage = productImageRepository.findById(id).orElse(null);
        if (productImage == null) {
            return ResponseEntity.badRequest().body("Product image not found");
        }
        productImageRepository.delete(productImage);
        return ResponseEntity.ok("Product image deleted");
    }

    @Override
    public void deleteAllProductImages() {
        productImageRepository.deleteAll();
    }

    public List<String> findProductImageUrlsByProductId(Long productId) {
        // Retrieve the list of ProductImage entities associated with the given productId
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);

        // If no ProductImage entities are found, return an empty list
        if (productImages.isEmpty()) {
            return Collections.emptyList();
        }

        // Map the list of ProductImage entities to a list of image URLs
        return productImages.stream()
                .map(ProductImage::getImagePath)
                .collect(Collectors.toList());
    }

    // Method to retrieve byte array of the image
    private byte[] getImageByteArray(ProductImage productImage) {
        try {
            Path imagePath = Paths.get(IMAGE_FOLDER_PATH + productImage.getImagePath());
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return null; // or throw exception if desired
        }
    }

    private String getImageUrl(String imagePath) {
        return IMAGE_FOLDER_PATH + imagePath;
    }
}
