package com.easygame.easygame.repository;

import com.easygame.easygame.model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface GroupRepository extends JpaRepository<GroupModel, Long> {
    boolean existsByCode(String code);
}
