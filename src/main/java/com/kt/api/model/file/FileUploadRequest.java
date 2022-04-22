package com.kt.api.model.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class FileUploadRequest {
    private String path;
    private Boolean isOverWrite;
    //private MultipartFile file;
}
