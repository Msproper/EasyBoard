package com.easygame.easygame.service;

import com.easygame.easygame.DTO.group.GroupRequestDTO;
import com.easygame.easygame.model.GroupModel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final UserService userService;

    public GroupModel save(GroupModel model){
        return repository.save(model);
    }

    public void createGroup(GroupRequestDTO requestDTO){
        UserModel user = userService.getCurrentUser();
        GroupModel groupModel = GroupModel.builder()
                .host(user)
                .name(requestDTO.getName())
                .code(generateUniqueCode())
                .build();
        save(groupModel);
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
