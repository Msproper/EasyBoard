package com.easygame.easygame.redis.model;

import com.easygame.easygame.enums.PermissionLevel;

public class BoardMember {
    private String username;
    private PermissionLevel permissionLevel;
    private boolean approved;
}