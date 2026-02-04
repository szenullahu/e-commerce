package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.BasketEntity;
import ch.sze.ecommerce.entity.dto.AddToBasketDTO;
import ch.sze.ecommerce.entity.dto.BasketResponseDTO;
import ch.sze.ecommerce.entity.dto.UpdateBasketItemDTO;
import ch.sze.ecommerce.service.BasketService;
import ch.sze.ecommerce.service.DTOMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketService basketService;
    private final DTOMapper dtoMapper;

    public BasketController(BasketService basketService, DTOMapper dtoMapper){
        this.basketService = basketService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<BasketResponseDTO> getMyBasket(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) return ResponseEntity.status(403).build();
        BasketEntity basket = basketService.getBasketForUser(userPrincipal.getUser());
        return ResponseEntity.ok(
                dtoMapper.toBasketDTO(basket)
        );
    }

    @PostMapping("/add")
    public ResponseEntity<BasketResponseDTO> addProductToBasket(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody AddToBasketDTO dto) {
        BasketEntity basket = basketService.addProductToBasket(userPrincipal.getUser(), dto.getProductID(), dto.getQuantity());
        return ResponseEntity.ok(
                dtoMapper.toBasketDTO(basket)

        );
    }

    @PutMapping("/update")
    public ResponseEntity<BasketResponseDTO> updateQuantity(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateBasketItemDTO dto) { // <--- 'int quantity' entfernt!

        BasketEntity updatedBasket = basketService.updateItemQuantity(principal.getUser(), dto.getProductId(), dto.getQuantity());
        return ResponseEntity.ok(
                dtoMapper.toBasketDTO(updatedBasket)
        );
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<BasketResponseDTO> removeItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable UUID productId) { // <--- @PathVariable hinzugefügt!

        BasketEntity basket = basketService.removeProductFromBasket(userPrincipal.getUser(), productId);
        return ResponseEntity.ok(
                dtoMapper.toBasketDTO(basket)
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearBasket(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        basketService.clearBasket(userPrincipal.getUser());
        return ResponseEntity.ok("Basket cleared successfully");
    }
}