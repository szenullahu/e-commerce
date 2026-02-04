package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddToBasketDTO {

    @NotNull
    private UUID productID;

    @Min(value = 1, message = "Amount must be more than one")
    private int quantity;
}
