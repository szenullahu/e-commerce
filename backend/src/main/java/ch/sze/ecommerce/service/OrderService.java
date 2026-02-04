package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.*;
import ch.sze.ecommerce.entity.dto.AddressDTO;
import ch.sze.ecommerce.repository.OrderRepo;
import ch.sze.ecommerce.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final BasketService basketService;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;


    public OrderService(BasketService basketService, OrderRepo orderRepo, ProductRepo productRepo) {
        this.basketService = basketService;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public OrderEntity placeOrder(UserEntity user, AddressDTO addressDTO) {
        BasketEntity basket = basketService.getBasketForUser(user);
        if(basket.getItems().isEmpty()){
            throw new IllegalArgumentException("Basket is empty.");
        }

        OrderEntity order = new OrderEntity();
        order.setStreet(addressDTO.getStreet());
        order.setCity(addressDTO.getCity());
        order.setZip(addressDTO.getZip());
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);

        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (BasketItemEntity basketItem : basket.getItems()) {
            ProductEntity product = basketItem.getProduct();

            if(basketItem.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("Product out of Stock. Stock available: " + product.getStock());
            }

            int newStock = product.getStock() - basketItem.getQuantity();
            product.setStock(newStock);

            productRepo.save(product);

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProduct(basketItem.getProduct());
            orderItem.setQuantity(basketItem.getQuantity());
            orderItem.setPricePerUnit(basketItem.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        updateOrderTotal(order);

        basketService.clearBasket(user);

        return orderRepo.save(order);
    }

    public OrderEntity getOrderDetails(UserEntity user, UUID orderId) {
        OrderEntity order =  orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID:" + orderId));

        if(!order.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Order not found with ID:" + orderId);
        }

        return order;
    }

    public List<OrderEntity> getOrdersForUser(UserEntity user) {
        return orderRepo.findByUserOrderByCreatedDesc(user);
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepo.findAll();
    }

    @Transactional
    public OrderEntity updateOrderStatus(UUID orderId, OrderStatus newStatus){
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if(order.getStatus() != OrderStatus.CANCELLED && newStatus == OrderStatus.CANCELLED) {
            returnStockToInventory(order);
        }

        order.setStatus(newStatus);
        return orderRepo.save(order);
    }

    private void returnStockToInventory(OrderEntity order) {
        for(OrderItemEntity item : order.getItems()) {
            ProductEntity product = item.getProduct();

            int newStock = product.getStock() + item.getQuantity();
            product.setStock(newStock);

            productRepo.save(product);
        }
    }

    private void updateOrderTotal(OrderEntity order) {
        double total = 0.0;
        for (OrderItemEntity item : order.getItems()) {
            total += item.getPricePerUnit() * item.getQuantity();
        }
        order.setTotalPrice(total);
    }
}
