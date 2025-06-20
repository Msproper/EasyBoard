package com.easygame.easygame.service;

import com.easygame.easygame.DTO.board.SnapshotDTO;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.SnapshotModel;
import com.easygame.easygame.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    public void saveSnapshot(Long boardId, SnapshotDTO snapshotDTO) {
        BoardModel board = boardService.findById(boardId);
        board.setAutoSaveSnapshot(new SnapshotModel(snapshotDTO));
        boardRepository.save(board);
    }

    public SnapshotDTO getSnapshot(Long boardId) {
        BoardModel board = boardService.findById(boardId);
        return new SnapshotDTO(board.getAutoSaveSnapshot());
    }
}
