package ch.sze.ecommerce.entity.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemResponseDTO {
    private UUID itemId;
    private UUID productId;
    private String productName;
    private byte[] productImage;
    private int quantity;
    private double pricePerUnit;
    private double totalLinePrice;
}
