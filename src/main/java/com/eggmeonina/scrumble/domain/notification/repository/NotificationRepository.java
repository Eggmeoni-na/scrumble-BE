package com.eggmeonina.scrumble.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eggmeonina.scrumble.domain.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByRecipientIdAndIdLessThanAndCreatedAtBetweenOrderByIdDesc(Long recipientId, Long id, LocalDateTime startDate, LocalDateTime endDate, Limit limit);

	@Query(
		"""
		SELECT n
		  FROM Notification n
		 WHERE n.id = :notificationId
		   AND n.readFlag = false
		"""
	)
	Optional<Notification> findByIdAndReadFlagNot(Long notificationId);
}
