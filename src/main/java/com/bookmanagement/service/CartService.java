package com.bookmanagement.service;

import com.bookmanagement.dto.BorrowRequest;
import com.bookmanagement.entity.Book;
import com.bookmanagement.entity.Cart;
import com.bookmanagement.entity.User;
import com.bookmanagement.repository.BookRepository;
import com.bookmanagement.repository.CartRepository;
import com.bookmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart addToCart(Long userId, BorrowRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Cart existingCart = cartRepository.findByUserAndBook(user, book).orElse(null);

        if (existingCart != null) {
            existingCart.setNumberOfBooks(existingCart.getNumberOfBooks() + request.getNumberOfBooks());
            return cartRepository.save(existingCart);
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setBook(book);
        cart.setNumberOfBooks(request.getNumberOfBooks());

        return cartRepository.save(cart);
    }

    public List<Cart> getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user);
    }

    public void removeFromCart(Long userId, Long cartId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        cartRepository.delete(cart);
    }

    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartRepository.deleteByUser(user);
    }
}

