package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.ProductRequestDTO;
import com.ecommerce.product.dto.ProductResponseDTO;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.model.ProductStatus;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        log.info("Creating a new product with SKU: {}", requestDTO.getSku());

        Product product = new Product();
        mapRequestToEntity(requestDTO, product);

        // Dynamically set status based on initial stock allocation
        if (product.getStockQuantity() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else {
            product.setStatus(ProductStatus.AVAILABLE);
        }

        Product savedProduct = productRepository.save(product);
        log.debug("Product successfully saved with ID: {}", savedProduct.getProductId());

        return mapToResponseDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        log.info("Fetching all products from database");
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product fetching failed. ID {} not found", id);
                    return new RuntimeException("Product not found with id: " + id);
                });

        return mapToResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        log.info("Updating product with ID: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product updating failed. ID {} not found", id);
                    return new RuntimeException("Product not found with id: " + id);
                });

        mapRequestToEntity(requestDTO, existingProduct);

        // Business Logic: Automatically manage status based on stock updates
        if (existingProduct.getStockQuantity() == 0) {
            existingProduct.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (existingProduct.getStatus() == ProductStatus.OUT_OF_STOCK) {
            existingProduct.setStatus(ProductStatus.AVAILABLE);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        log.debug("Product ID {} successfully updated", id);

        return mapToResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Attempting to delete product with ID: {}", id);

        if (!productRepository.existsById(id)) {
            log.error("Deletion failed. Product ID {} does not exist", id);
            throw new RuntimeException("Cannot delete. Product not found with id: " + id);
        }

        productRepository.deleteById(id);
        log.debug("Product ID {} removed from database", id);
    }

    // --- Private DTO Mapping Helper Methods ---

    private void mapRequestToEntity(ProductRequestDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSku(dto.getSku());
        entity.setPrice(dto.getPrice());
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setImageUrl(dto.getImageUrl());
    }

    private ProductResponseDTO mapToResponseDTO(Product entity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(entity.getProductId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSku(entity.getSku());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setImageUrl(entity.getImageUrl());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}