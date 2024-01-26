package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.RoleType;
import com.jinnnii.pass.domain.converter.ActiveStatusConverter;
import com.jinnnii.pass.domain.converter.RoleTypeConverter;
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

    @Convert(converter = RoleTypeConverter.class) @Column(nullable = false)
    private RoleType role;

    @Convert(converter =  ActiveStatusConverter.class) @Column(nullable = false)
    private ActiveStatus status;

    private String region;
    private Integer phone;

    @Lob
    private String meta;
}
