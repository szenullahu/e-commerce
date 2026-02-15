package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.*;
import ch.sze.ecommerce.entity.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DTOMapper {

    public UserResponseDTO toUserDTO (UserEntity user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setRole(user.getRole());

        return dto;
    }

    public OrderResponseDTO toOrderDTO (OrderEntity order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setCreated(order.getCreated());
        dto.setOrderStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());

        dto.setStreet(order.getStreet());
        dto.setCity(order.getCity());
        dto.setZip(order.getZip());

        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(this::toOrderItemDTO)
                .toList();
        dto.setItems(itemDTOs);
        return dto;
    }

    public OrderItemResponseDTO toOrderItemDTO(OrderItemEntity item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setItemId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPricePerUnit(item.getPricePerUnit());

        dto.setTotalLinePrice(item.getPricePerUnit() * item.getQuantity());

        if(item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductImage(item.getProduct().getImage());
        } else {
            dto.setProductName("Product not available");
        }

        return dto;
    }

    public BasketResponseDTO toBasketDTO(BasketEntity basket) {
        BasketResponseDTO dto = new BasketResponseDTO();
        dto.setBasketId(basket.getId());
        dto.setTotalPrice(basket.getTotalPrice());

        dto.setItems(basket.getItems().stream()
                .map(this::toBasketItemDTO)
                .collect(Collectors.toList()));

        dto.setTotalItemsCount(basket.getItems().stream()
                .mapToInt(BasketItemEntity::getQuantity)
                .sum());

        return dto;
    }

    private BasketItemResponseDTO toBasketItemDTO(BasketItemEntity item) {
        BasketItemResponseDTO dto = new BasketItemResponseDTO();
        dto.setItemId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPricePerUnit(item.getPrice());
        dto.setTotalLinePrice(item.getPrice() * item.getQuantity());

        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductImage(item.getProduct().getImage());
        }

        return dto;
    }
}
