package com.easygame.easygame.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionLevel {
    ADMIN(3),
    OWNER(4),  // Владелец (макс. права)
    EDITOR(2), // Редактор
    VIEWER(1); // Наблюдатель

    private final int level;

}
