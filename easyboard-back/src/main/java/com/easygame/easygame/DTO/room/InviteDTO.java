package com.easygame.easygame.DTO.room;

import com.easygame.easygame.enums.InviteStatus;
import com.easygame.easygame.redis.model.Invite;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class
InviteDTO {
    private InviteStatus status;
    private String sender;
    private String boardTitle;
    private String id;
    private String uuid;
    public static InviteDTO accepted(String username, String title, String uuid) {
        return InviteDTO.builder()
                .status(InviteStatus.ACCEPTED)
                .sender(username)
                .boardTitle(title)
                .uuid(uuid)
                .build();
    }

    public static InviteDTO banned(String username, String title) {
        return InviteDTO.builder()
                .status(InviteStatus.BANNED)
                .sender(username)
                .boardTitle(title)
                .build();
    }

    public static InviteDTO pending(String sender, String title, String id) {
        return InviteDTO.builder()
                .status(InviteStatus.PENDING)
                .sender(sender)
                .boardTitle(title)
                .id(id)
                .build();
    }
}
