package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Entity
@ToString(callSuper = true)
@Table(name = "entrance")
public class EntranceEntity extends AuditingField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entranceId;

    @ManyToOne(optional = false) @JoinColumn(name="passId")
    private PassEntity passEntity;

    @OneToOne(optional = false) @JoinColumn(name="seatId")
    private SeatEntity seatEntity;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ActiveStatus status;
    private String qrCode;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}