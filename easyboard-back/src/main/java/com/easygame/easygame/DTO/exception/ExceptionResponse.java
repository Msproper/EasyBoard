package com.easygame.easygame.DTO.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ при ошибках")
public class ExceptionResponse {
    @Schema(description = "Сообщение с информацией о ошибке", example = "Bad request")
    private String message;
}
