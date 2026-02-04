package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.entity.OrderEntity;
import ch.sze.ecommerce.entity.dto.OrderResponseDTO;
import ch.sze.ecommerce.entity.dto.UpdateOrderStatusDTO;
import ch.sze.ecommerce.service.DTOMapper;
import ch.sze.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminOrderController {

    private final OrderService orderService;
    private final DTOMapper dtoMapper;

    public AdminOrderController(OrderService orderService, DTOMapper dtoMapper) {
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders () {
        List<OrderEntity> orders = orderService.getAllOrders();
        List<OrderResponseDTO> dtos = orders.stream()
                .map(dtoMapper::toOrderDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable UUID orderId, @RequestBody UpdateOrderStatusDTO dto) {
        return ResponseEntity.ok(dtoMapper.toOrderDTO(orderService.updateOrderStatus(orderId, dto.getStatus())));
    }

}
