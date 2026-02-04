package ch.sze.ecommerce.entity.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BasketItemResponseDTO {
    private UUID itemId;
    private UUID productId;
    private String productName;
    private byte[] productImage;
    private double pricePerUnit;
    private int quantity;
    private double totalLinePrice;
}
