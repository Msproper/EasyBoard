package com.easygame.easygame.DTO;
import com.easygame.easygame.model.UserModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoResponse {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatar;

    public UserInfoResponse(UserModel userModel) {
        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.email = userModel.getEmail();
        this.avatar = userModel.getAvatar();
    }
}
