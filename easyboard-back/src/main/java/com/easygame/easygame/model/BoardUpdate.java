package com.easygame.easygame.model;

import com.easygame.easygame.enums.UpdateType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardModel board;

    @Column(nullable = false, columnDefinition = "JSONB") // Для PostgreSQL
    private String payload; // Данные изменения (JSON)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserModel author;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private UpdateType type; // CREATE, UPDATE, DELETE, MOVE, etc.
}

