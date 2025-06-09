package com.easygame.easygame.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_invites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessInvite {

    @Id
    private String uuid; // UUID, по которому переходит пользователь (используется в URL)

    @Column(unique = true, nullable = false, length = 8)
    private String code; // Уникальный короткий код (7–8 символов)

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "boardId")
    private BoardModel board;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long ttlSeconds; // Время жизни (в секундах)

    public boolean isExpired() {
        return createdAt.plusSeconds(ttlSeconds).isBefore(LocalDateTime.now());
    }
}
