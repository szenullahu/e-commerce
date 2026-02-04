package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.CategoryEntity;
import ch.sze.ecommerce.entity.dto.CategoryDTO;
import ch.sze.ecommerce.repository.CategoryRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepo.findAll();
    }

    public CategoryEntity getCategory(UUID uuid) {
        return categoryRepo.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        if (categoryRepo.existsByName(categoryEntity.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        return categoryRepo.save(categoryEntity);
    }

    public CategoryEntity updateCategory(UUID id, CategoryDTO dto) {
        CategoryEntity categoryEntity = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryEntity.setName(dto.getName());
        return categoryRepo.save(categoryEntity);
    }

    public void deleteCategory(UUID id) {
        CategoryEntity categoryEntity = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryRepo.delete(categoryEntity);
    }
}
