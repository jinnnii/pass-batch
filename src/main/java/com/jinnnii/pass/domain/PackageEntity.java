package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.PackageType;
import com.jinnnii.pass.domain.converter.PackageTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@NoArgsConstructor
@Table(name = "package")
public class PackageEntity extends AuditingField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @Setter @ManyToOne(optional = false) @JoinColumn(name="placeId", nullable = false)
    private PlaceEntity placeEntity;

    @Setter @Column(nullable = false)
    private String packageName;

    @Setter @Column(nullable = false)
    private Integer price;

    @Setter @Column(nullable = false)
    private Integer period;

    @Setter @Convert(converter = PackageTypeConverter.class) @Column(nullable = false)
    private PackageType type;

    public static PackageEntity of(PlaceEntity placeEntity, String packageName, Integer price, Integer period, PackageType type) {
        return new PackageEntity(placeEntity, packageName,price,period,type);
    }

    private PackageEntity(PlaceEntity placeEntity, String packageName, Integer price, Integer period, PackageType type) {
        this.placeEntity = placeEntity;
        this.packageName = packageName;
        this.price = price;
        this.period = period;
        this.type = type;
    }
}
