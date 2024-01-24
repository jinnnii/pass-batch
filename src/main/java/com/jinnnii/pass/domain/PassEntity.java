package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.EnterStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * [입장권]
 * - 사용자는 여러 개의 입장권을 가질 수 있다. / 입장권은 한 명의 사용자만 가질 수 있다.
 * - 패키지는 여러 개의 입장권을 가질 수 있다. / 입장권은 한 개의 패키지만 가질 수 있다.
 * - [insert] : userId, packageId, status,remainingTime/started-endedAt
 * - [update] : status, remainingTime, expiredAt
 */
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "pass")
public class PassEntity extends AuditingField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passId;

    @ManyToOne(optional = false) @JoinColumn(name = "userId", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(optional = false) @JoinColumn(name = "packageId")
    private PackageEntity packageEntity;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EnterStatus status;

    private LocalDateTime remainingTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime expiredAt;


    @OneToMany(mappedBy = "passEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    @OrderBy("createdAt desc")
    private Set<EntranceEntity> entrances = new LinkedHashSet<>();
}
