package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.CategoryEntity;
import ch.sze.ecommerce.entity.ProductEntity;
import ch.sze.ecommerce.entity.dto.ProductDTO;
import ch.sze.ecommerce.repository.CategoryRepo;
import ch.sze.ecommerce.repository.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<ProductEntity> getAllProducts() {
        return productRepo.findAll();
    }

    public ProductEntity getProduct(UUID id) {
        return productRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public ProductEntity createProduct(ProductDTO dto) {
        if (productRepo.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
        CategoryEntity categoryEntity = categoryRepo.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(dto.getName());
        productEntity.setDescription(dto.getDescription());
        productEntity.setPrice(dto.getPrice());
        productEntity.setStock(dto.getStock());
        productEntity.setImage(dto.getImage());
        productEntity.setCategoryEntity(categoryEntity);
        return productRepo.save(productEntity);
    }

    public ProductEntity updateProduct(UUID id, ProductDTO dto) {
        ProductEntity productEntity = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        productEntity.setName(dto.getName());
        productEntity.setDescription(dto.getDescription());
        productEntity.setImage(dto.getImage());
        productEntity.setPrice(dto.getPrice());
        productEntity.setStock(dto.getStock());
        CategoryEntity categoryEntity = categoryRepo.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        productEntity.setCategoryEntity(categoryEntity);

        return productRepo.save(productEntity);
    }

    public void deleteProduct(UUID id) {
        CategoryEntity categoryEntity = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryRepo.delete(categoryEntity);
    }
}
