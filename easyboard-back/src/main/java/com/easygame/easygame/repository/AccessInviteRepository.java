package com.easygame.easygame.repository;

import com.easygame.easygame.model.AccessInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessInviteRepository extends JpaRepository<AccessInvite, String> {
    Optional<AccessInvite> findByCode(String code);
    boolean existsByCode(String code);
}
