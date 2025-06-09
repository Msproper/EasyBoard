package com.easygame.easygame.service;

import com.easygame.easygame.DTO.exception.ExpiredInviteException;
import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.model.AccessInvite;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.repository.AccessInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessInviteService {
    private final AccessInviteRepository repository;
    private final BoardService boardService;

    public AccessInvite generateAccessInvite(Long boardId) {
        String uuid = UUID.randomUUID().toString();
        String code = generateUniqueCode();
        BoardModel board =  boardService.findById(boardId);

        AccessInvite invite = new AccessInvite();
        invite.setUuid(uuid);
        invite.setCode(code);
        invite.setBoard(board);
        invite.setCreatedAt(LocalDateTime.now());
        invite.setTtlSeconds(60L);

        return repository.save(invite);
    }

    public AccessInvite validateByUuid(String uuid) {
        AccessInvite invite = repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Приглашение по ссылке не найдено"));
        if (invite.isExpired()) {
            repository.deleteById(uuid);
            throw new ExpiredInviteException("Срок действия ссылки истек");
        }
        return invite;
    }

    public AccessInvite validateByCode(String code) {
        AccessInvite invite = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Приглашение по коду не найдено"));
        if (invite.isExpired()) {
            repository.delete(invite);
            throw new ExpiredInviteException("Срок действия кода истек");
        }
        return invite;
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
