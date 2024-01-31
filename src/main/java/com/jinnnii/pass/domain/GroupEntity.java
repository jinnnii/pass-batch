package com.jinnnii.pass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Entity
@Table(name = "user_group")
public class GroupEntity extends AuditingField{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false) private String groupName;
    @Lob private String description;

    @OneToMany(mappedBy = "group")
    private Set<UserGroupEntity> userGroups = new LinkedHashSet<>();

    public static GroupEntity of(String groupName, String description){
        return new GroupEntity(groupName, description);
    }
    private GroupEntity(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }
}
