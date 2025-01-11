package com.eggmeonina.scrumble.domain.notification.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static jakarta.persistence.GenerationType.*;

import com.eggmeonina.scrumble.common.domain.BaseEntity;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_id", nullable = false)
	private Member recipient;

	@Column(name = "notification_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	@Column(name = "read_flag", nullable = false)
	private boolean readFlag;

	@Column(name = "notification_data", nullable = true)
	private String notificationData;

	@Column(name ="notification_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationStatus notificationStatus;

	@Builder(builderMethodName = "create")
	public Notification(Member recipient, NotificationType notificationType, boolean readFlag,
		String notificationData, NotificationStatus notificationStatus) {
		this.recipient = recipient;
		this.notificationType = notificationType;
		this.readFlag = readFlag;
		this.notificationData = notificationData;
		this.notificationStatus = notificationStatus;

		initValid(recipient, notificationType);
	}

	private void initValid(Member recipient, NotificationType notificationType){
		if(recipient == null){
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		if(notificationType == null){
			throw new ExpectedException(NOTIFICATION_TYPE_NOT_NULL);
		}
	}

	// 알림 읽기 여부와 상태를 변경한다.
	public void updateNotification(boolean readFlag, NotificationStatus notificationStatus){
		this.readFlag = readFlag;
		this.notificationStatus = notificationStatus;
	}

	public void checkSameRecipient(Member member){
		if(!getRecipient().equals(member)){
			throw new ExpectedException(UNAUTHORIZED_ACCESS);
		}
	}

	@Override
	public String toString() {
		return "Notification{" +
			"id=" + id +
			", notificationType=" + notificationType +
			", readFlag=" + readFlag +
			", notificationData='" + notificationData + '\'' +
			", notificationStatus=" + notificationStatus +
			'}';
	}
}
