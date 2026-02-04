package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateBasketItemDTO {
    @NotNull
    private UUID productId;

    @Min(1)
    private int quantity;
}
