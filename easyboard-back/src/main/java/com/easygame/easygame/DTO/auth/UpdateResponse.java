package com.easygame.easygame.DTO.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ на обновление пользователя по токену")
public class UpdateResponse {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя", example = "Mike")
    private String username;
    @Schema(description = "Почта", example = "test@gmail.com")
    private String email;
    @Schema(description = "Статус аккаунта", example = "true")
    private Boolean enabled;
}
