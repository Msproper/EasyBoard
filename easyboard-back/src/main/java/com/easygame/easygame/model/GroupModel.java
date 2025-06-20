package com.easygame.easygame.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.util.*;


@Entity
@Data
@Builder
@Table(name="Groups")
public class GroupModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="host_id")
    private UserModel host;

    @Column
    private String code;

    @ManyToMany
    @JoinTable(
            name="group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserModel> members = new HashSet<>();

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    private BoardModel board;

    public void addMember(UserModel user){
        members.add(user);
    }
}
