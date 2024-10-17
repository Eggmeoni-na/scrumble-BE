package com.eggmeonina.scrumble.domain.todo.notification.domain;

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

	@Builder(builderMethodName = "create")
	public Notification(Member recipient, NotificationType notificationType, boolean readFlag,
		String notificationData) {
		this.recipient = recipient;
		this.notificationType = notificationType;
		this.readFlag = readFlag;
		this.notificationData = notificationData;

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

	public void read(){
		this.readFlag = true;
	}
}
