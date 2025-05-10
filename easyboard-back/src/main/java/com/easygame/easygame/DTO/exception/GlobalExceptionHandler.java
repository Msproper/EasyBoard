package com.easygame.easygame.DTO.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(new ExceptionResponse("Ошибка:" + " " +e.getMessage()), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(DuplicateInviteException.class)
    public ResponseEntity<?> handleRuntimeException(DuplicateInviteException e) {
        return new ResponseEntity<>(new ExceptionResponse("Ошибка:" + " " +e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e){
        return new ResponseEntity<>(new ExceptionResponse("Ошибка поиска: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionDeniedException(PermissionDeniedException e) {
        return new ResponseEntity<>(new ExceptionResponse("Ошибка доступа: "+e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(new ExceptionResponse("Данный файл не поддерживается."), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ValidationRuntimeException.class)
    public ResponseEntity<?> handleValidationRuntimeException(ValidationRuntimeException e){
        System.out.println(e.getBindingResult().getFieldError());
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Ошибка при проверке: ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("; ");
        }
        return new ResponseEntity<>(new ExceptionResponse(errorMessage.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleTokenTimeExpiration(ExpiredJwtException e){
        return new ResponseEntity<>(new ExceptionResponse("Истекло время сессии"), HttpStatus.UNAUTHORIZED);
    }

}
