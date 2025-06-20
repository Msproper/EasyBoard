package com.easygame.easygame.DTO.board;

import com.easygame.easygame.model.AccessInvite;
import com.easygame.easygame.model.BoardModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessInviteDTO {
    @Schema(description = "UUID для ссылки", example = "a1a1-1a1a-1a1a-a1a1")
    private String uuid; // UUID, по которому переходит пользователь (используется в URL)

    @Schema(description = "Сгенерированный код", example = "DfdsaSdsd")
    private String code; // Уникальный короткий код (7–8 символов)


    public AccessInviteDTO(AccessInvite accessInvite) {
        this.code = accessInvite.getCode();
        this.uuid = accessInvite.getUuid();
    }
}
