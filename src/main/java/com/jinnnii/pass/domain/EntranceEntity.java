package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.converter.ActiveStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * [입장권]
 * - 이용권은 여러 개의 입장권을 가질 수 있다. / 입장권은 한 개의 이용권만 가질 수 있다.
 * - 자리는 한 개의 입장권을 가질 수 있다. / 입장권은 한 개의 자리만 가질 수 있다.
 * - [insert] : passId, status, qrcode, startedAt
 * - [update] : status, endedAt
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(name = "entrance")
public class EntranceEntity extends AuditingField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entranceId;

    @ManyToOne(optional = false) @JoinColumn(name="passId")
    private PassEntity passEntity;

    @OneToOne(optional = false) @JoinColumn(name="seatId") @Setter
    private SeatEntity seatEntity;

    @Convert(converter = ActiveStatusConverter.class) @Column(nullable = false) @Setter
    private ActiveStatus status;
    private String qrCode;

    private LocalDateTime startedAt;
    @Setter private LocalDateTime endedAt;

    public static EntranceEntity of(PassEntity passEntity, SeatEntity seatEntity, ActiveStatus status, LocalDateTime startedAt) {
        return new EntranceEntity(passEntity, seatEntity, status, null, startedAt);
    }

    private EntranceEntity(PassEntity passEntity, SeatEntity seatEntity, ActiveStatus status, String qrCode, LocalDateTime startedAt) {
        this.passEntity = passEntity;
        this.seatEntity = seatEntity;
        this.status = status;
        this.qrCode = qrCode;
        this.startedAt = startedAt;
    }
}