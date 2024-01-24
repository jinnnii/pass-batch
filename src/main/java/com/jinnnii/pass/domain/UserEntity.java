package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString(callSuper = true)
@Table(name = "user")
public class UserEntity extends AuditingField{
    @Id
    private String userId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ActiveStatus status;

    private String region;
    private Integer phone;
    private String meta;
}
