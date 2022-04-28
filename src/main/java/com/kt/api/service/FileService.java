package com.kt.api.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.kt.api.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 사용자가 지정한 패스에 업로드
     * /pv 경로 + upload + 사용자 지정경로
     * @param file
     * @param path
     */
    public void save(MultipartFile file,String path) {
        try {
            String targetPath=this.uploadPath + path;
            if (!Files.exists(Paths.get(targetPath))) {
                FileUtils.createDir(targetPath);
            }
            Files.copy(file.getInputStream(), Paths.get(targetPath).resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
//    public Resource load(String filename) {
//        try {
//            Path file = Paths.get(uploadPath)
//                    .resolve(filename);
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read the file!");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Error: " + e.getMessage());
//        }
//    }
//
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(Paths.get(uploadPath)
//                .toFile());
//    }
//
//    public List<Path> loadAll() {
//        try {
//            Path root = Paths.get(uploadPath);
//            if (Files.exists(root)) {
//                return Files.walk(root, 1)
//                        .filter(path -> !path.equals(root))
//                        .collect(Collectors.toList());
//            }
//
//            return Collections.emptyList();
//        } catch (IOException e) {
//            throw new RuntimeException("Could not list the files!");
//        }
//    }
}
