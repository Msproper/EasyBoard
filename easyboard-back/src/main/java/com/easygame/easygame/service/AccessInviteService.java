package com.easygame.easygame.service;

import com.easygame.easygame.DTO.board.AccessInviteDTO;
import com.easygame.easygame.DTO.exception.ExpiredInviteException;
import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.DTO.exception.PermissionDeniedException;
import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.model.AccessInvite;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.repository.AccessInviteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessInviteService {
    private final AccessInviteRepository repository;
    private final BoardService boardService;
    private final RoomManager roomManager;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AccessInviteService.class);

    public InviteDTO acceptInviteByCode(String code) {
        AccessInvite invite = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Приглашение по ссылке не найдено"));
        BoardModel board = validateAccessInvite(invite).getBoard();
        return acceptAccess(board);
    }

    public InviteDTO acceptInviteByUUID(String uuid) {
        AccessInvite invite = repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Приглашение по ссылке не найдено"));
        BoardModel board = validateAccessInvite(invite).getBoard();
        return acceptAccess(board);
    }

    public AccessInviteDTO generateAccessInvite(Long boardId) {
        BoardModel board =  boardService.findById(boardId);
        UserModel user = userService.getCurrentUser();
        if (board.getOwner() != user) throw new PermissionDeniedException("Нельзя создать код для чужих досок");

        Optional<AccessInvite> foundInvite = repository.findByBoard(boardService.findById(boardId));
        if (foundInvite.isPresent()) {
            try {
                validateAccessInvite(foundInvite.get());
                return new AccessInviteDTO(foundInvite.get());
            } catch (Exception e) {
                logger.warn("Existing invite is invalid, generating new one", e);
            }
        }
        String uuid = UUID.randomUUID().toString();
        String code = generateUniqueCode();
        AccessInvite invite = AccessInvite.builder()
                .board(board)
                .code(code)
                .createdAt(LocalDateTime.now())
                .ttlSeconds(6000L)
                .uuid(uuid)
                .build();

        return new AccessInviteDTO(repository.save(invite));
    }

    private InviteDTO acceptAccess(BoardModel board) {
        var user = userService.getCurrentUser();
        boardService.addMember(user, board);
        String uuid = roomManager.upsertRoom(board, user.getUsername());
        return InviteDTO.accepted(user.getUsername(), board.getTitle(), uuid);
    }

    private AccessInvite validateAccessInvite(AccessInvite accessInvite) {
        if (accessInvite.isExpired()) {
            repository.deleteById(accessInvite.getUuid());
            throw new ExpiredInviteException("Срок действия ссылки истек");
        }
        return accessInvite;
    }

    private String generateUniqueCode() {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Без O/0/I/1
        String code;
        do {
            code = new Random().ints(7, 0, chars.length())
                    .mapToObj(chars::charAt)
                    .map(Object::toString)
                    .collect(Collectors.joining());
        } while (repository.existsByCode(code)); // Гарантия уникальности
        return code;
    }
}
