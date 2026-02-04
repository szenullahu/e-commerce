package ch.sze.ecommerce.repository;

import ch.sze.ecommerce.entity.BasketEntity;
import ch.sze.ecommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BasketRepo extends JpaRepository<BasketEntity, UUID> {

    Optional<BasketEntity> findByUser(UserEntity user);

}
