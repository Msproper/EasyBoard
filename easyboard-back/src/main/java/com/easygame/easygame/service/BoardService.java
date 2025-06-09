package com.easygame.easygame.service;

import com.easygame.easygame.DTO.board.BoardRequestDTO;

import com.easygame.easygame.DTO.board.BoardResponseDTO;
import com.easygame.easygame.DTO.board.SnapshotDTO;
import com.easygame.easygame.DTO.exception.FileProcessingException;
import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.DTO.exception.PermissionDeniedException;
import com.easygame.easygame.enums.SearchSort;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.SnapshotModel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.repository.BoardRepository;
import com.easygame.easygame.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final FileService fileService;
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private BoardModel save(BoardModel board) {
        return boardRepository.save(board);
    }

    public BoardResponseDTO create(BoardRequestDTO boardRequestDTO, MultipartFile photo){
        String filename = processUploadedPhoto(photo);

        var board = BoardModel.builder()
                .title(boardRequestDTO.getTitle())
                .description(boardRequestDTO.getDescription())
                .isPublic(boardRequestDTO.getIsPublic())
                .owner(userService.getCurrentUser())
                .imageUrl(filename)
                .uuid(UUID.randomUUID().toString())
                .build();
        logger.info("Доска {} успешно создана", boardRequestDTO.getTitle());
        return new BoardResponseDTO(save(board));
    }

    public void saveSnapshot(Long boardId, SnapshotDTO snapshotDTO){
        BoardModel board = findById(boardId);
        board.setAutoSaveSnapshot(new SnapshotModel(snapshotDTO));
        save(board);
    }

    public BoardModel findById(long id){
        return boardRepository.findById(id).orElseThrow(()->new NotFoundException("Указанная доска не найдена"));
    }

    private String processUploadedPhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return null;
        }

        try {
            return fileService.saveFile(photo);
        } catch (IOException e) {
            logger.error("Failed to save board photo", e);
            throw new FileProcessingException("Failed to save board photo", e);
        }
    }

    public List<BoardResponseDTO> getBoards(){
        var user = userService.getCurrentUser();
        var boards = boardRepository.findByOwner(user);
        return boards.stream().map(BoardResponseDTO::new).toList();
    }

    public void update(BoardRequestDTO boardRequestDTO, Long id){
        BoardModel foundBoard = findById(id);
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
        BoardModel foundBoard = findById(id);
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
                .map(result -> result.stream()
                        .filter(boardModel -> !boardModel.getOwner().equals(user)) // Исключаем доски текущего пользователя
                        .map(boardModel -> new BoardResponseDTO(
                                boardModel,
                                boardModel.isUserMember(user),
                                boardModel.isUserBanned(user)))
                        .toList())
                .orElse(Collections.emptyList());
    }



    public SnapshotDTO getSnapshot(Long id) {
        BoardModel foundBoard = findById(id);

        return new SnapshotDTO(foundBoard.getAutoSaveSnapshot());
    }
}
