package com.easygame.easygame.DTO.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
public class BoardRequestDTO {
    @Schema(description = "Имя", example = "MyClassRoom")
    @Size(min = 2, max = 40, message = "Имя слишком длинное или короткое")
    @NotBlank(message = "Имя не может быть пустыми")
    private String title;

    @Schema(description = "Описание", example = "Моя комната")
    private String description;

    @Getter
    @Schema(description = "Публичная ли комната", example = "true")
    @NotNull(message = "Не выбран тип комнаты")
    private Boolean isPublic;

}
