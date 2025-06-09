package com.easygame.easygame.repository;

import com.easygame.easygame.model.SnapshotModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<SnapshotModel, Long> {
    // Найти последний снимок по дате создания
    Optional<SnapshotModel> findTopByOrderByCreatedAtDesc();
}
