package ch.sze.ecommerce.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;

    @Min(value = 0, message = "Price must be 0 or greater")
    private double price;

    @Min(value = 0, message = "Stock must be 0 or greater")
    private int stock;

    @NotNull(message = "Image cannot be null")
    private byte[] image;

    @NotNull(message = "Category cannot be null")
    private UUID categoryId;
}
