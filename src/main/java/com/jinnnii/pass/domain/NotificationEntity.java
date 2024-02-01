package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.NotificationEvent;
import com.jinnnii.pass.domain.converter.NotificationEventConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "notification")
public class NotificationEntity extends AuditingField {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationSeq;
    @Column(nullable = false) private String uuid;
    @Column(nullable = false) private String text;
    @Convert(converter = NotificationEventConverter.class)
    @Column(nullable = false) private NotificationEvent event;
    @Column(nullable = false) private Boolean sent;
    private LocalDateTime sentAt;
}
