package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.SeatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
/**
 * [자리]
 * - 자리는 한 개의 플레이스를 가질 수 있다. / 플레이스는 여러 개의 자리를 가질 수 있다.
 * - [insert] : placeId, seatName, type, x, y
 * - [delete] : seatId
 */
@Getter
@Entity
@ToString(callSuper = true)
@Table(name = "seat")
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(optional = false) @JoinColumn(name="placeId", nullable = false)
    private PlaceEntity placeEntity;

    private String seatName;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private SeatType type;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

}
