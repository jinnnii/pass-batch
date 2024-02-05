package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.SeatType;
import com.jinnnii.pass.domain.converter.SeatTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * [자리]
 * - 자리는 한 개의 플레이스를 가질 수 있다. / 플레이스는 여러 개의 자리를 가질 수 있다.
 * - [insert] : placeId, seatName, type, x, y
 * - [delete] : seatId
 */
@Getter
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "seat")
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(optional = false) @JoinColumn(name="placeId", nullable = false)
    private PlaceEntity placeEntity;

    private String seatName;

    @Convert(converter = SeatTypeConverter.class) @Column(nullable = false)
    private SeatType type;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    public static SeatEntity of(PlaceEntity placeEntity, String seatName, SeatType type, Integer x, Integer y){
        return new SeatEntity(placeEntity, seatName, type, x, y);
    }
    private SeatEntity(PlaceEntity placeEntity, String seatName, SeatType type, Integer x, Integer y) {
        this.placeEntity = placeEntity;
        this.seatName = seatName;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
