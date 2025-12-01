package com.bookmanagement.repository;

import com.bookmanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(String genre);
    List<Book> findByStatus(Book.BookStatus status);
    
    @Query("SELECT b FROM Book b WHERE b.price >= :minPrice AND b.price < :maxPrice")
    List<Book> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT b FROM Book b WHERE b.genre = :genre AND b.price >= :minPrice AND b.price < :maxPrice")
    List<Book> findByGenreAndPriceRange(@Param("genre") String genre, 
                                        @Param("minPrice") Double minPrice, 
                                        @Param("maxPrice") Double maxPrice);
}

