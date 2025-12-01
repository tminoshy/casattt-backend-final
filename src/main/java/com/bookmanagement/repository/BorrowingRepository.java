package com.bookmanagement.repository;

import com.bookmanagement.entity.Borrowing;
import com.bookmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByUser(User user);
    List<Borrowing> findByUserAndReturnDateIsNull(User user);
    Optional<Borrowing> findByUserAndBookAndReturnDateIsNull(User user, com.bookmanagement.entity.Book book);
    List<Borrowing> findAllByOrderByBorrowDateDesc();
}

