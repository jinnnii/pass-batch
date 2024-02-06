package com.jinnnii.pass.domain;

import com.jinnnii.pass.adapter.KakaoTalkMessageRequest.*;
import com.jinnnii.pass.domain.constant.BulkStatus;
import com.jinnnii.pass.domain.converter.BulkStatusConverter;
import com.jinnnii.pass.domain.converter.TemplateObjectConverter;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name="bulk_notification")
public class BulkNotificationEntity extends AuditingField{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bulkNotificationSeq;

    @ManyToOne(optional = false) @JoinColumn(name = "placeId", nullable = false)
    private PlaceEntity placeEntity;

    @Convert(converter = BulkStatusConverter.class) @Column(nullable = false)
    private BulkStatus status;

    @Convert(converter = TemplateObjectConverter.class) @Column(nullable = false)
    private TemplateObject text;

    @Column(nullable = false) private LocalDateTime bookedAt;

    public static BulkNotificationEntity of(PlaceEntity placeEntity, BulkStatus status, TemplateObject text, LocalDateTime bookedAt){
        return new BulkNotificationEntity(placeEntity, status, text, bookedAt);
    }
    private BulkNotificationEntity(PlaceEntity placeEntity, BulkStatus status, TemplateObject text, LocalDateTime bookedAt) {
        this.placeEntity = placeEntity;
        this.status = status;
        this.text = text;
        this.bookedAt = bookedAt;
    }
}
