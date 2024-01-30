package com.jinnnii.pass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@Entity
@Table(name="user_group_mapping")
public class UserGroupEntity extends AuditingField{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupSeq;

    @ManyToOne(optional = false) @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne(optional = false) @JoinColumn(name = "groupId", nullable = false)
    private GroupEntity group;

    public static UserGroupEntity of(UserEntity user, GroupEntity group){
        return new UserGroupEntity(user, group);
    }
    private UserGroupEntity(UserEntity user, GroupEntity group) {
        this.user = user;
        this.group = group;
    }
}
