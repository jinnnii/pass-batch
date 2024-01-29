package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.converter.ActiveStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * [플레이스]
 * - 플레이스는 한 명의 사용자를 가질 수 있다. / 사용자는 한 개의 플레이스만 가질 수 있다. (여러 개 가질 수 도 있으나.. 뷰의 편의를 위해 하나로 설정)
 * - 자리는 한 개의 입장권을 가질 수 있다. / 입장권은 한 개의 자리만 가질 수 있다.
 * - [insert] : passId, status, qrcode, startedAt
 * - [update] : status, endedAt
 */
@Getter
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "place")
public class PlaceEntity extends AuditingField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(nullable = false) private String placeName;
    
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private UserEntity userEntity;

    private String address;
    private Double latitude;
    private Double longitude;

    @Convert(converter = ActiveStatusConverter.class) @Column(nullable = false)
    private ActiveStatus status;

    @OneToMany(mappedBy = "placeEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<SeatEntity> seats = new LinkedHashSet<>();

    public static PlaceEntity of(String placeName, UserEntity userEntity, ActiveStatus status){
        return new PlaceEntity(placeName, userEntity, null, null, null, status, null);
    }

    private PlaceEntity(String placeName, UserEntity userEntity, String address, Double latitude, Double longitude, ActiveStatus status, Set<SeatEntity> seats) {
        this.placeName = placeName;
        this.userEntity = userEntity;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.seats = seats;
    }
}
