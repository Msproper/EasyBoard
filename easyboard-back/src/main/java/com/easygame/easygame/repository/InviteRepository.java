package com.easygame.easygame.repository;

import com.easygame.easygame.enums.InviteStatus;
import com.easygame.easygame.redis.model.Invite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InviteRepository extends CrudRepository<Invite, String> {
    // Кастомный метод для проверки
    default boolean existsActiveInvite(String sender, String boardId) {
        String id = Invite.generateId(sender, boardId);
        Optional<Invite> invite = findById(id);
        System.out.println(id);
        return invite.isPresent() &&
                invite.get().getStatus() == InviteStatus.PENDING;
    }

    // Поиск по получателю
    List<Invite> findBySenderUsernameAndBoard(String sender, String boardId);
}
