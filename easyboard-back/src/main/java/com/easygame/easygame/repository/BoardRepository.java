package com.easygame.easygame.repository;

import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<BoardModel, Long> {
    List<BoardModel> findByOwner(UserModel owner);
    Page<BoardModel> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}