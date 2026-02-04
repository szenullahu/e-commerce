package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.OrderEntity;
import ch.sze.ecommerce.entity.dto.AddressDTO;
import ch.sze.ecommerce.entity.dto.OrderResponseDTO;
import ch.sze.ecommerce.service.DTOMapper;
import ch.sze.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    private final DTOMapper dtoMapper;

    public OrderController(OrderService orderService, DTOMapper dtoMapper) {
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders (@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<OrderEntity> orders = orderService.getOrdersForUser(userPrincipal.getUser());

        List<OrderResponseDTO> dtos = orders.stream()
                .map(dtoMapper::toOrderDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetails (@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable UUID orderId) {
        OrderEntity order = orderService.getOrderDetails(userPrincipal.getUser(), orderId);
        return ResponseEntity.ok(dtoMapper.toOrderDTO(order));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> placeOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody AddressDTO addressDTO) {
        OrderEntity createdOrder = orderService.placeOrder(userPrincipal.getUser(), addressDTO);
        return ResponseEntity.ok(dtoMapper.toOrderDTO(createdOrder));
    }
}
