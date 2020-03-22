package ru.itis.servlets.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // название файла в хранилище
    private String storageFileName;
    // название файла оригинальное
    private String originalFileName;
    // размер файла
    private Long size;
    // тип файла (MIME)
    private String type;
    // по какому URL можно получить файл
    private String url;
    //расширение
    private String extension;

    public static FileInfo getInstance(MultipartFile multipartFile) {
        String extension = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf('.'));
        String storageName = UUID.randomUUID().toString() + extension;
        String originalName = multipartFile.getOriginalFilename();
        String url = "localhost/files/" + storageName;
        String type = multipartFile.getContentType();
        Long size = multipartFile.getSize();
        return FileInfo.builder()
                .storageFileName(storageName)
                .size(size)
                .originalFileName(originalName)
                .extension(extension)
                .url(url)
                .type(type)
                .build();
    }
}
