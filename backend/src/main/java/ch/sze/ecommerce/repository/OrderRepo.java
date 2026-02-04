package ch.sze.ecommerce.repository;

import ch.sze.ecommerce.entity.OrderEntity;
import ch.sze.ecommerce.entity.OrderStatus;
import ch.sze.ecommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByUser(UserEntity user);
    List<OrderEntity> findByUserOrderByCreatedDesc(UserEntity user);
    List<OrderEntity> findByStatus(OrderStatus status);
}
