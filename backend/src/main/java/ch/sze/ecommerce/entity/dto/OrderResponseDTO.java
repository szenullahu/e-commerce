package ch.sze.ecommerce.entity.dto;

import ch.sze.ecommerce.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDTO {
    private UUID orderId;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "Europe/Zurich")
    private Date created;

    private OrderStatus orderStatus;
    private double totalPrice;

    private String street;
    private String city;
    private String zip;

    private List<OrderItemResponseDTO> items;

}