package com.easygame.easygame.DTO.board;

import com.easygame.easygame.model.SnapshotModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotDTO {
    private String data;

    public SnapshotDTO(SnapshotModel snapshotModel){
        if (snapshotModel == null) this.data = null;
        else this.data = snapshotModel.getData();
    }
}
