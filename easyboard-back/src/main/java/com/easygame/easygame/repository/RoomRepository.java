package com.easygame.easygame.repository;

import com.easygame.easygame.enums.InviteStatus;
import com.easygame.easygame.redis.model.Invite;
import com.easygame.easygame.redis.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, String> {


    // Поиск по получателю
    Optional<Room> findByUuid(String uuid);
}
