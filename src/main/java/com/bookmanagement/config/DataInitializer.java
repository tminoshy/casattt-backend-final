package com.bookmanagement.config;

import com.bookmanagement.entity.Book;
import com.bookmanagement.entity.User;
import com.bookmanagement.entity.Borrowing;
import com.bookmanagement.entity.*;
import com.bookmanagement.repository.CartRepository;
import com.bookmanagement.repository.UserRepository;
import com.bookmanagement.repository.BookRepository;
import com.bookmanagement.repository.BorrowingRepository;
import com.bookmanagement.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BookRepository bookRepository;

    private final CartRepository cartRepository;

    private final NotificationRepository notificationRepository;

    private final BorrowingRepository borrowingRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        // Create default admin if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin3");
            admin.setPassword("secret");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@bookmanagement.com");
            admin.setPhoneNumber("0000000000");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin created: username=admin, password=secret");
        }

        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setFirstName("First" + i);
            user.setLastName("Last" + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setPhoneNumber("012345678" + i);
            user.setSex(i % 2 == 0 ? "Male" : "Female");
            user.setDateOfBirth(null);
            user.setPassword("password" + i);
            user.setRole(User.Role.USER);

            userRepository.save(user);
        }

        if (bookRepository.count() == 0) {
            List<Book> books = List.of(
                    new Book(0, "The Java Handbook", "John Doe", "100% new", 29.99, null,
                            "Comprehensive guide to Java.", 10, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Spring Boot in Action", "Jane Smith", "99% like new", 35.50, null,
                            "Practical guide to Spring Boot.", 8, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Learning Python", "Mark Lutz", "95%", 40.0, null,
                            "Python programming for beginners.", 5, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Effective Java", "Joshua Bloch", "100% new", 45.0, null,
                            "Best practices in Java programming.", 7, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Clean Code", "Robert C. Martin", "90%", 38.0, null,
                            "A handbook of agile software craftsmanship.", 6, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Java Concurrency in Practice", "Brian Goetz", "95%", 50.0, null,
                            "Mastering concurrency in Java.", 4, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Design Patterns", "Erich Gamma", "100% new", 42.0, null,
                            "Elements of reusable object-oriented software.", 5, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Algorithms Unlocked", "Thomas Cormen", "90%", 33.0, null,
                            "Introduction to algorithms.", 9, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "Head First Java", "Kathy Sierra", "99%", 37.0, null,
                            "Fun guide to Java programming.", 12, "Programming", Book.BookStatus.AVAILABLE),
                    new Book(0, "The Pragmatic Programmer", "Andrew Hunt", "100% new", 39.0, null,
                            "Journey to mastery in software development.", 8, "Programming", Book.BookStatus.AVAILABLE)
            );

            System.out.println("here");

            bookRepository.saveAll(books);

            // --------------------------------------
            // 3. Create 10 Borrowings
            // --------------------------------------
            for (int i = 1; i <= 10; i++) {
                User user = userRepository.findById((long) i).get();
                Book book = bookRepository.findById((long) i).get();

                Borrowing borrowing = new Borrowing();
                borrowing.setUser(user);
                borrowing.setBook(book);
                borrowing.setBorrowDate(LocalDate.now().minusDays(9));
                borrowing.setReturnDate(null);
                borrowing.setNumberOfBooks(1 + random.nextInt(3));
                borrowing.setStatus("Not Return Yet");

                borrowingRepository.save(borrowing);
            }

            // --------------------------------------
            // 4. Create 10 Carts
            // --------------------------------------
            for (int i = 1; i <= 10; i++) {
                User user = userRepository.findById((long) i).get();
                Book book = bookRepository.findById((long) i).get();

                Cart cart = new Cart();
                cart.setUser(user);
                cart.setBook(book);
                cart.setNumberOfBooks(1);

                cartRepository.save(cart);
            }

            // --------------------------------------
            // 5. Create 10 Notifications
            // --------------------------------------
            for (int i = 1; i <= 10; i++) {
                User user = userRepository.findById((long) i).get();
                Book book = bookRepository.findById((long) i).get();
                Borrowing borrowing = borrowingRepository.findById((long) i).get();

                Notification notification = Notification.builder()
                        .user(user)
                        .book(book)
                        .borowing(borrowing)
                        .issueAt(LocalDateTime.now().minusDays(9))
                        .notiticationStatus(Notification.NotificationStatus.SUCCESSFULLY_BORROWED)
                        .message("Borrowed " + book.getBookName())
                        .build();

                notificationRepository.save(notification);
            }
            System.out.println("10 default books created!");
        }
    }
}

