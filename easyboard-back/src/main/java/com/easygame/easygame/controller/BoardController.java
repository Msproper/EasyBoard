package com.easygame.easygame.controller;


import com.easygame.easygame.DTO.board.BoardRequestDTO;
import com.easygame.easygame.DTO.board.BoardResponseDTO;
import com.easygame.easygame.DTO.board.SnapshotDTO;
import com.easygame.easygame.DTO.exception.ValidationRuntimeException;
import com.easygame.easygame.enums.SearchSort;
import com.easygame.easygame.service.BoardService;
import com.easygame.easygame.service.SnapshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "АPI для досок")
public class BoardController {

    private final BoardService boardService;
    private final SnapshotService snapshotService;

    @Operation(summary = "Создание доски")
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBlankBoard(
            @RequestPart("data") @Valid BoardRequestDTO boardRequestDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        BoardResponseDTO boardResponseDTO = boardService.create(boardRequestDTO, photo);
        return new ResponseEntity<>(boardResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Получение досок по текущему пользователю")
    @GetMapping
    public ResponseEntity<?> getBoards(@RequestParam(required = false) String query,
                                   @RequestParam(defaultValue = "10") @Min(1) int limit,
                                   @RequestParam(defaultValue = "0") @Min(0) @Max(100) int page,
                                   @RequestParam(required = false, defaultValue = "TITLE_ASC") SearchSort sort) {
        List<BoardResponseDTO> boardResponseDTOS = boardService.getMyBoards(query, limit, page, sort);
        return new ResponseEntity<>(boardResponseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Получение залайканных досок по текущему пользователю")
    @GetMapping("/liked")
    public ResponseEntity<?> getLikedBoards(@RequestParam(required = false) String query,
                                       @RequestParam(defaultValue = "10") @Min(1) int limit,
                                       @RequestParam(defaultValue = "0") @Min(0) @Max(100) int page,
                                       @RequestParam(required = false, defaultValue = "TITLE_ASC") SearchSort sort) {
        List<BoardResponseDTO> boardResponseDTOS = boardService.getMyLikedBoards(query, limit, page, sort);
        return new ResponseEntity<>(boardResponseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Изменение доски по id")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody @Valid BoardRequestDTO boardRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        boardService.update(id, boardRequestDTO);
        return new ResponseEntity<>("Успешно изменено", HttpStatus.OK);
    }

    @Operation(summary = "Удаление доски по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBoards(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "10") @Min(1) int limit,
            @RequestParam(defaultValue = "0") @Min(0) @Max(100) int page,
            @RequestParam(required = false, defaultValue = "TITLE_ASC") SearchSort sort
    ) {
        List<BoardResponseDTO> boards = boardService.searchBoards(query, limit, page, sort);
        return ResponseEntity.ok(boards);
    }

    @PostMapping("/{id}/snapshot")
    public ResponseEntity<?> saveSnapshot(@PathVariable Long id, @RequestBody SnapshotDTO snapshotDTO){
        snapshotService.saveSnapshot(id,snapshotDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{id}/snapshot")
    public ResponseEntity<?> getSnapshot(@PathVariable Long id){
        SnapshotDTO snapshotDTO = snapshotService.getSnapshot(id);
        return new ResponseEntity<>(snapshotDTO, HttpStatus.OK);
    }


    @PostMapping("/likes/{boardId}")
    private ResponseEntity<?> addLike(@PathVariable Long boardId){
        boardService.likeBoard(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/likes/{boardId}")
    private ResponseEntity<?> removeLike(@PathVariable Long boardId){
        boardService.unlikeBoard(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

