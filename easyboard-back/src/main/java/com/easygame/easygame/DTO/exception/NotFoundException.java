package com.easygame.easygame.DTO.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
