package com.easygame.easygame.service;

import com.easygame.easygame.DTO.board.BoardRequestDTO;

import com.easygame.easygame.DTO.board.BoardResponseDTO;
import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.DTO.exception.PermissionDeniedException;
import com.easygame.easygame.enums.SearchSort;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private BoardModel save(BoardModel board) {
        return boardRepository.save(board);
    }

    public BoardResponseDTO create(BoardRequestDTO boardRequestDTO){

        var board = BoardModel.builder()
                .title(boardRequestDTO.getTitle())
                .description(boardRequestDTO.getDescription())
                .isPublic(boardRequestDTO.getIsPublic())
                .owner(userService.getCurrentUser())
                .build();
        logger.info("Доска {} успешно создана", boardRequestDTO.getTitle());
        return new BoardResponseDTO(save(board));
    }

    public List<BoardResponseDTO> getBoards(){
        var user = userService.getCurrentUser();
        var boards = boardRepository.findByOwner(user);
        return boards.stream().map(BoardResponseDTO::new).toList();
    }

    public void update(BoardRequestDTO boardRequestDTO, Long id){
        BoardModel foundBoard = boardRepository.findById(id).orElseThrow(()-> new NotFoundException("доска не была найдена"));
        if (foundBoard.getOwner().equals(userService.getCurrentUser())){
            throw new PermissionDeniedException("Нельзя изменять доски, не созданные вами");
        }
        foundBoard.setTitle(boardRequestDTO.getTitle());
        foundBoard.setDescription(boardRequestDTO.getDescription());
        foundBoard.setPublic(boardRequestDTO.getIsPublic());
        save(foundBoard);
    }


    /**
     * Удаление доски по id
     *
     * @param id - id доски, которую нужно удалить
     */
    public void delete(Long id){
        BoardModel foundBoard = boardRepository.findById(id).orElseThrow(()-> new NotFoundException("доска не была найдена"));
        if (foundBoard.getOwner().equals(userService.getCurrentUser())){
            throw new PermissionDeniedException("Нельзя удалять доски, не созданные вами");
        }
        else boardRepository.delete(foundBoard);
    }

    /**
     * Метод для поиска досок по заголовку с пагинацией и сортировкой
     *
     * @param query Строка поискового запроса
     * @param limit Количество элементов на странице
     * @param page Номер страницы (начинается с 0)
     * @param sort Тип сортировки результатов
     * @return Список досок в формате DTO, соответствующих критериям поиска
     */
    public List<BoardResponseDTO> searchBoards(String query, int limit, int page, SearchSort sort) {
        Pageable pageable = PageRequest.of(
                page,
                limit,
                sort.getSortValue()
        );

        UserModel user = userService.getCurrentUser();

        return Optional.ofNullable(boardRepository.findByTitleContainingIgnoreCase(query, pageable))
                .map(result -> result.map((boardModel)-> new BoardResponseDTO(boardModel, boardModel.isUserMember(user), boardModel.isUserBanned(user))).toList())
                .orElse(Collections.emptyList());
    }

}
