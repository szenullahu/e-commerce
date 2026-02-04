package ch.sze.ecommerce.repository;

import ch.sze.ecommerce.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, UUID> {
    boolean existsByName(String name);
}
