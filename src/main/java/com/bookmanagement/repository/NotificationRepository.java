package com.bookmanagement.repository;

import com.bookmanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public List<Notification> findByUserId(Long userId);
    @Query("""
    SELECT COUNT(n) FROM Notification n
    WHERE n.user.id = :userId
    AND n.message = :message
    AND n.notiticationStatus = :status
""")
    Long countWarnings(
            @Param("userId") Long userId,
            @Param("message") String message,
            @Param("status") Notification.NotificationStatus status
    );

    default boolean existsWarning(Long userId, String message) {
        return countWarnings(userId, message, Notification.NotificationStatus.WARNING) > 0;
    }


}
