package ch.sze.ecommerce.entity.dto;

import ch.sze.ecommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    @NotNull
    private OrderStatus status;

    private String trackingNumber;
    private String cancelReason;
}
