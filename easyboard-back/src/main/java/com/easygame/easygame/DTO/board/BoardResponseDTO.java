package com.easygame.easygame.DTO.board;

import com.easygame.easygame.model.BoardModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(description = "Ответ на запросы по доске")
@Getter
@AllArgsConstructor
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
    @Schema(description = "Является ли юзер участником доски", example = "true")
    private Boolean isMember = false;
    @Schema(description = "Является ли юзер заблокированным в доске", example = "true")
    private Boolean isBlocked = false;
    public BoardResponseDTO(BoardModel boardModel, boolean isMember, boolean isBlocked) {
        this.id = boardModel.getId();
        this.createAt = boardModel.getCreatedAt().toString();
        this.updateAt = boardModel.getUpdatedAt().toString();
        this.title = boardModel.getTitle();
        this.description = boardModel.getDescription();
        this.owner = boardModel.getOwner().getUsername();
        this.isMember = isMember;
        this.isBlocked = isBlocked;
    }

    public BoardResponseDTO(BoardModel boardModel) {
        this.id = boardModel.getId();
        this.createAt = boardModel.getCreatedAt().toString();
        this.updateAt = boardModel.getUpdatedAt().toString();
        this.title = boardModel.getTitle();
        this.description = boardModel.getDescription();
        this.owner = boardModel.getOwner().getUsername();

    }
}
