package com.easygame.easygame.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;  // Путь к папке для хранения файлов

    public String saveFile(MultipartFile file) throws IOException {
        // Генерация уникального имени файла (например, по времени)
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Формируем путь к файлу
        Path path = Paths.get(uploadDir, filename);

        // Проверка, существует ли директория
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());  // Если директория не существует, создаем её
        }

        // Сохраняем файл в файловую систему
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return filename;  // Возвращаем имя файла
    }


    public byte[] loadFile(String filename) throws IOException {
        // Загружаем файл из файловой системы
        Path path = Paths.get(uploadDir, filename);
        return Files.readAllBytes(path);
    }
}

