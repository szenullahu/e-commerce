package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.BasketEntity;
import ch.sze.ecommerce.entity.BasketItemEntity;
import ch.sze.ecommerce.entity.ProductEntity;
import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.repository.BasketRepo;
import ch.sze.ecommerce.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class BasketService {

    private final BasketRepo basketRepo;
    private final ProductRepo productRepo;

    public BasketService (BasketRepo basketRepo, ProductRepo productRepo) {
        this.basketRepo = basketRepo;
        this.productRepo = productRepo;
    }


    public BasketEntity getBasketForUser(UserEntity user) {
        return basketRepo.findByUser(user).orElseThrow(() -> new IllegalArgumentException("User missing basket"));
    }

    @Transactional
    public BasketEntity addProductToBasket(UserEntity user, UUID productId, int quantity) {
        BasketEntity basket = getBasketForUser(user);
        ProductEntity product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<BasketItemEntity> existingItem = basket.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();

        if(existingItem.isPresent()) {
            BasketItemEntity item = existingItem.get();
            int newTotalQuantity = item.getQuantity() + quantity;

            if(newTotalQuantity > product.getStock()) {
                throw new IllegalArgumentException("Product out of Stock. Stock available: " + product.getStock());
            }
            item.setQuantity(newTotalQuantity);
            item.setPrice(product.getPrice());
        } else {

            if(quantity > product.getStock()) {
                throw new IllegalArgumentException("Product out of Stock: Stock available: " + product.getStock());
            }
            BasketItemEntity newItem = new BasketItemEntity();
            newItem.setProduct(product);
            newItem.setBasket(basket);
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(quantity);

            basket.getItems().add(newItem);
        }

        updateBasketTotal(basket);

        return basketRepo.save(basket);
    }

    @Transactional
    public BasketEntity updateItemQuantity (UserEntity user, UUID productId, int newQuantity) {
        BasketEntity basket = getBasketForUser(user);
        BasketItemEntity item = basket.getItems().stream().filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in basket"));

        int availableStock = item.getProduct().getStock();
        if (newQuantity > availableStock) {
            throw new IllegalArgumentException("Product out of Stock: Stock available: \" + product.getStock())");
        }

        item.setQuantity(newQuantity);
        updateBasketTotal(basket);

        return basketRepo.save(basket);
    }

    @Transactional
    public BasketEntity removeProductFromBasket(UserEntity user, UUID productId) {
        BasketEntity basket = getBasketForUser(user);

        boolean removed = basket.getItems().removeIf(item-> item.getProduct().getId().equals(productId));

        if(!removed) {
            throw  new RuntimeException("Product not in basket");
        }

        updateBasketTotal(basket);
        return basketRepo.save(basket);
    }

    private void updateBasketTotal(BasketEntity basket) {
        double total = 0.0;
        for(BasketItemEntity item : basket.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        basket.setTotalPrice(total);
    }

    public void clearBasket(UserEntity user) {
        BasketEntity basket = getBasketForUser(user);

        basket.getItems().clear();

        basket.setTotalPrice(0.0);
        basketRepo.save(basket);
    }
}
