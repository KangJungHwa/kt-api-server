package com.kt.api.model.file;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileData {
    private String filename;
    private String path;
    private String lastModifyDate;
    private String fileSize;
}
