package com.easygame.easygame.controller;


import com.easygame.easygame.DTO.board.BoardRequestDTO;
import com.easygame.easygame.DTO.board.BoardResponseDTO;
import com.easygame.easygame.DTO.exception.ValidationRuntimeException;
import com.easygame.easygame.enums.SearchSort;
import com.easygame.easygame.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "АPI для досок")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "Создание доски")
    @PostMapping("/create")
    public ResponseEntity<?> createBlankBoard(@RequestBody @Valid BoardRequestDTO boardRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        BoardResponseDTO boardResponseDTO = boardService.create(boardRequestDTO);
        return new ResponseEntity<>(boardResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Получение досок по текущему пользователю")
    @GetMapping("/")
    public ResponseEntity<?> getBoards() {
        List<BoardResponseDTO> boardResponseDTOS = boardService.getBoards();
        return new ResponseEntity<>(boardResponseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Изменение доски по id")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody @Valid BoardRequestDTO boardRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        boardService.update(boardRequestDTO, id);
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
}

