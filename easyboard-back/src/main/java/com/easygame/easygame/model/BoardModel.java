package com.easygame.easygame.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "boards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_name", nullable = false)
    private UserModel owner;

//    @Column(columnDefinition = "JSONB")
//    private String data;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "board_members", // Название соединительной таблицы
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<UserModel> members = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "board_banned_users", // Название соединительной таблицы
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<UserModel> bannedUsers = new HashSet<>();

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "background_image_url")
    private String backgroundImageUrl;

    public void addMember(UserModel user) {
        members.add(user);
    }
    public void addBannedUser(UserModel user) {
        bannedUsers.add(user);
    }

    public void removeMember(UserModel user) {
        members.removeIf(member -> member.equals(user));
    }

    public void removeBannedUser(UserModel user) {
        bannedUsers.removeIf(member -> member.equals(user));
    }

    public boolean isUserMember(UserModel user) {
        return members.contains(user);
    }

    public boolean isUserBanned(UserModel user) {
        return bannedUsers.contains(user);
    }
}
