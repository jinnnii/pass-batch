package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.BulkStatus;
import com.jinnnii.pass.domain.converter.BulkStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "bulk_pass")
public class BulkPassEntity extends AuditingField{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bulkPassSeq;

    @ManyToOne(optional = false) @JoinColumn(name = "groupId")
    private GroupEntity groupEntity;

    @ManyToOne(optional = false) @JoinColumn(name = "packageId")
    private PackageEntity packageEntity;

    @Column(nullable = false) @Convert(converter = BulkStatusConverter.class)
    private BulkStatus status;

    private LocalTime time;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static BulkPassEntity of(GroupEntity groupEntity, PackageEntity packageEntity, BulkStatus status, LocalTime period, LocalDateTime startedAt, LocalDateTime endedAt){
        return new BulkPassEntity(groupEntity, packageEntity, status, period, startedAt, endedAt);
    }

    private BulkPassEntity(GroupEntity groupEntity, PackageEntity packageEntity, BulkStatus status, LocalTime time, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.groupEntity = groupEntity;
        this.packageEntity = packageEntity;
        this.status = status;
        this.time = time;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
