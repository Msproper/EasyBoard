package com.easygame.easygame.DTO.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationRuntimeException extends RuntimeException {
    private final BindingResult bindingResult;

    public ValidationRuntimeException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
