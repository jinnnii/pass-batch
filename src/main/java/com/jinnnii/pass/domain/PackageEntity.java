package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.PackageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
/**
 * [패키지]
 * - 패키지는 한 개의 플레이스만 가질 수 있다. / 플레이스는 여러 개의 패키지를 가질 수 있다.
 * - [insert] : placeId, packageName, price, period, type
 * - [update] : packageName, price, period, type
 * - [delete] : placeId
 */
@Getter
@Entity
@ToString(callSuper = true)
@Table(name = "package")
public class PackageEntity extends AuditingField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @ManyToOne(optional = false) @JoinColumn(name="placeId", nullable = false)
    private PlaceEntity placeEntity;

    @Column(nullable = false)
    private String packageName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer period;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PackageType type;
}
