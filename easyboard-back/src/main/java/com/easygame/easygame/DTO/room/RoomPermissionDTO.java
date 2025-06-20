package com.easygame.easygame.DTO.room;

import com.easygame.easygame.enums.PermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPermissionDTO {
    private PermissionLevel permissionLevel;


}
