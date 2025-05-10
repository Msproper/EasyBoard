package com.easygame.easygame.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DetailsRequest {

    private String name;
    private String surname;
    private String status;

    @Override
    public String toString() {
        return "DetailsRequest{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}