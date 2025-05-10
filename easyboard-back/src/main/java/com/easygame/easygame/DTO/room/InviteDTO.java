package com.easygame.easygame.DTO.room;

import com.easygame.easygame.enums.InviteStatus;
import com.easygame.easygame.redis.model.Invite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class
InviteDTO {
    private InviteStatus status;
    private String sender;
    private String boardTitle;
    private String id;
    private String timeStamp;
    private String boardId;
    private String uuid;


    public InviteDTO(Invite invite) {
        this.boardTitle = invite.getBoardTitle();
        this.sender = invite.getSenderUsername();
        this.id = invite.getId();
        this.status = invite.getStatus();
        this.timeStamp = invite.getTimestamp().toString();
        this.boardId = invite.getBoardId();
    }
}
