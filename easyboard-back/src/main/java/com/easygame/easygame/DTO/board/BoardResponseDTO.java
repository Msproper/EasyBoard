package com.easygame.easygame.DTO.board;

import com.easygame.easygame.model.BoardModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Ответ на запросы по доске")
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class BoardResponseDTO {
    @Schema(description = "ID доски", example = "1")
    private Long id;
    @Schema(description = "Дата создания", example = "22.02.2022")
    private String createAt;
    @Schema(description = "Дата изменения", example = "22.02.2022")
    private String updateAt;
    @Schema(description = "Название доски", example = "MyClass")
    private String title;
    @Schema(description = "Описание доски", example = "Its my classroom board")
    private String description;
    @Schema(description = "Имя создателя доски", example = "Urii")
    private String owner;
    @Schema(description = "Есть ли доступ", example = "true")
    private Boolean isAccess = false;
    @Schema(description = "Является ли юзер заблокированным в доске", example = "true")
    private Boolean isBlocked = false;
    @Schema(description = "Путь к изображению", example = "photo.img")
    private String imageUrl;
    public BoardResponseDTO(BoardModel boardModel, boolean isAccess, boolean isBlocked) {
        this.id = boardModel.getId();
        this.createAt = boardModel.getCreatedAt().toString();
        this.updateAt = boardModel.getUpdatedAt().toString();
        this.title = boardModel.getTitle();
        this.description = boardModel.getDescription();
        this.owner = boardModel.getOwner().getUsername();
        this.isAccess = isAccess;
        this.isBlocked = isBlocked;
        this.imageUrl = boardModel.getImageUrl();
    }

    public BoardResponseDTO(BoardModel boardModel) {
        this.id = boardModel.getId();
        this.createAt = boardModel.getCreatedAt().toString();
        this.updateAt = boardModel.getUpdatedAt().toString();
        this.title = boardModel.getTitle();
        this.description = boardModel.getDescription();
        this.owner = boardModel.getOwner().getUsername();
        this.imageUrl = boardModel.getImageUrl();
        this.isAccess = true;
    }
}
