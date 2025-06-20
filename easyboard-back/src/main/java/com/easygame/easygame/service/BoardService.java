package com.easygame.easygame.service;

import com.easygame.easygame.DTO.board.BoardRequestDTO;

import com.easygame.easygame.DTO.board.BoardResponseDTO;
import com.easygame.easygame.DTO.exception.FileProcessingException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(BoardService.class);

    public BoardResponseDTO create(BoardRequestDTO request, MultipartFile photo) {
        String filename = processPhoto(photo);
        UserModel currentUser = userService.getCurrentUser();

        BoardModel board = BoardModel.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isPublic(request.getIsPublic())
                .owner(currentUser)
                .imageUrl(filename)
                .uuid(UUID.randomUUID().toString())
                .likesCount(0)
                .build();

        BoardModel saved = save(board);
        logger.info("Доска '{}' создана пользователем '{}'", request.getTitle(), currentUser.getUsername());

        return new BoardResponseDTO(saved, currentUser.getLikedBoards().contains(saved));
    }

    public void update(Long boardId, BoardRequestDTO request) {
        BoardModel board = findBoardOwnedByCurrentUser(boardId);

        board.setTitle(request.getTitle());
        board.setDescription(request.getDescription());
        board.setPublic(request.getIsPublic());

        save(board);
    }

    public void delete(Long boardId) {
        BoardModel board = findBoardOwnedByCurrentUser(boardId);
        boardRepository.delete(board);
    }

    public List<BoardResponseDTO> getMyBoards(String query, int limit, int page, SearchSort sort) {
        Pageable pageable = PageRequest.of(page, limit, sort.getSortValue());
        UserModel currentUser = userService.getCurrentUser();
        var likedBoard = currentUser.getLikedBoards();
        return boardRepository.findByOwnerAndTitleContainingIgnoreCase(currentUser, query,pageable)
                .stream()
                .map(boardModel -> new BoardResponseDTO(boardModel, likedBoard.contains(boardModel)))
                .toList();
    }

    public List<BoardResponseDTO> getMyLikedBoards(String query, int limit, int page, SearchSort sort) {
            UserModel currentUser = userService.getCurrentUser();
            Set<BoardModel> likedBoards = currentUser.getLikedBoards();

            // Фильтрация по query (если query не пустой)
            Stream<BoardModel> filteredStream = likedBoards.stream()
                    .filter(board -> query == null || query.isEmpty() ||
                            board.getTitle().toLowerCase().startsWith(query.toLowerCase()));

            // Сортировка
            filteredStream = switch (sort) {
                case TITLE_ASC -> filteredStream.sorted(Comparator.comparing(BoardModel::getTitle));
                case TITLE_DESC -> filteredStream.sorted(Comparator.comparing(BoardModel::getTitle).reversed());
                case CREATEDAT_ASC -> filteredStream.sorted(Comparator.comparing(BoardModel::getCreatedAt));
                case CREATEDAT_DESC -> filteredStream.sorted(Comparator.comparing(BoardModel::getCreatedAt).reversed());
                default -> filteredStream;
            };

            // Пагинация
            List<BoardModel> result = filteredStream
                    .skip((long) page * limit)
                    .limit(limit)
                    .toList();
            return result.stream().map(board -> new BoardResponseDTO(
                    board,
                    true
            )).toList();
    }

    public List<BoardResponseDTO> searchBoards(String query, int limit, int page, SearchSort sort) {
        Pageable pageable = PageRequest.of(page, limit, sort.getSortValue());
        UserModel currentUser = userService.getCurrentUser();


        return boardRepository.findByTitleContainingIgnoreCase(query, pageable)
                .stream()
                .filter(board -> !board.getOwner().equals(currentUser))
                .map(board -> new BoardResponseDTO(
                        board,
                        board.isUserMember(currentUser),
                        board.isUserBanned(currentUser),
                        currentUser.getLikedBoards().contains(board)
                        ))
                .toList();
    }



    public BoardModel findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Указанная доска не найдена"));
    }

    public void addMember(UserModel user, BoardModel board){
        if (!board.getMembers().contains(user)) {
            board.addMember(user);
        }
        save(board);
    }

    public void addBan(UserModel user, BoardModel board){
        if (!board.getBannedUsers().contains(user)) {
            board.addBannedUser(user);
        }
        save(board);
    }

    private BoardModel save(BoardModel board){
        return boardRepository.save(board);
    }

    private BoardModel findBoardOwnedByCurrentUser(Long id) {
        BoardModel board = findById(id);
        UserModel currentUser = userService.getCurrentUser();

        if (!board.getOwner().equals(currentUser)) {
            throw new PermissionDeniedException("Вы не являетесь владельцем этой доски");
        }

        return board;
    }

    private String processPhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) return null;

        try {
            return fileService.saveFile(photo);
        } catch (IOException e) {
            logger.error("Ошибка при сохранении изображения доски", e);
            throw new FileProcessingException("Не удалось сохранить изображение", e);
        }
    }

    public void likeBoard(Long boardId) {
        BoardModel board = findById(boardId);
        UserModel user = userService.getCurrentUser();
        if (user.getLikedBoards().add(board)) {
            board.setLikesCount(board.getLikesCount() + 1);
            userService.save(user);
        }
    }

    public void unlikeBoard(Long boardId) {
        BoardModel board = findById(boardId);
        UserModel user = userService.getCurrentUser();
        if (user.getLikedBoards().remove(board)) {
            board.setLikesCount(Math.max(0, board.getLikesCount() - 1));
            userService.save(user);
        }
    }
}
