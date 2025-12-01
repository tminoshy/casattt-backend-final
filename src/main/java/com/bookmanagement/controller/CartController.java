package com.bookmanagement.controller;

import com.bookmanagement.dto.BorrowRequest;
import com.bookmanagement.entity.Cart;
import com.bookmanagement.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addToCart(@PathVariable Long userId, @RequestBody BorrowRequest request) {
        try {
            Cart cart = cartService.addToCart(userId, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Cart>> getCart(@PathVariable Long userId) {
        List<Cart> cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}/{cartId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long userId, @PathVariable Long cartId) {
        try {
            cartService.removeFromCart(userId, cartId);
            return ResponseEntity.ok("Item removed from cart");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

