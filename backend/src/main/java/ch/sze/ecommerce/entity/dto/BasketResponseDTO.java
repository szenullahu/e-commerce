package ch.sze.ecommerce.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BasketResponseDTO {
    private UUID basketId;
    private List<BasketItemResponseDTO> items;
    private double totalPrice;
    private int totalItemsCount;
}
