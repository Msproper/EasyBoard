package com.easygame.easygame.controller;

import com.easygame.easygame.service.FileService;
import com.easygame.easygame.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;  // Сервис для работы с файлами

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> fdasd(){
        System.out.println("YEAS");
        return ResponseEntity.ok("Photo uploaded successfully");
    }

    // Эндпоинт для загрузки фотографии пользователя
    @PostMapping("/uploadAvatar")
    public ResponseEntity<String> uploadPhoto(@RequestParam("photo") MultipartFile file) {
        userService.saveAvatar(file);
        return ResponseEntity.ok("Photo uploaded successfully");
    }

    @GetMapping("/photo")
    public ResponseEntity<?> getPhoto(@RequestParam("filePath") String filePath) {
        try {
            byte[] photo = fileService.loadFile(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

