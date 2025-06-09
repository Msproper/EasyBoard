package com.easygame.easygame.service;

import com.easygame.easygame.DTO.board.SnapshotDTO;
import com.easygame.easygame.model.BoardModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotService {
    private final BoardService boardService;


}
