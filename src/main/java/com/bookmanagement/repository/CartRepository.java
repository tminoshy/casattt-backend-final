package com.bookmanagement.repository;

import com.bookmanagement.entity.Cart;
import com.bookmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndBook(User user, com.bookmanagement.entity.Book book);
    void deleteByUser(User user);
}

