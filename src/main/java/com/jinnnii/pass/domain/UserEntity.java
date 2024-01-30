package com.jinnnii.pass.domain;

import com.jinnnii.pass.domain.constant.ActiveStatus;
import com.jinnnii.pass.domain.constant.RoleType;
import com.jinnnii.pass.domain.converter.ActiveStatusConverter;
import com.jinnnii.pass.domain.converter.RoleTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "user_account")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserGroupEntity> userGroups = new LinkedHashSet<>();

    public static UserEntity of(String userId, String password, RoleType role, ActiveStatus status){
        return new UserEntity(userId, password, role,status, null, null,null);
    }

    private UserEntity(String userId, String password, RoleType role, ActiveStatus status, String region, Integer phone, String meta) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.status = status;
        this.region = region;
        this.phone = phone;
        this.meta = meta;
    }
}
