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


//    public void createSymbolicLink(Path source, Path target) {
//        try {
//            Files.createSymbolicLink(source,target);
//            //Files.createDirectories(Paths.get(uploadPath));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not create upload folder!");
//        }
//    }
//    public void save(MultipartFile file) {
//        try {
//            Path root = Paths.get(uploadPath);
//            if (!Files.exists(root)) {
//                init();
//            }
//            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
//        } catch (Exception e) {
//            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//        }
//    }

    /**
     * 사용자가 지정한 패스에 업로드
     * /pv 경로 + upload + 사용자 지정경로
     * @param file
     * @param path
     */
    public void save(MultipartFile file,String path) {
        try {
            String linkPath=this.uploadPath = this.uploadPath + path;

            //파일 체크를 할때 symboliclink가 있는지 체크를  해야 할 수도 있음.
            // java.nio.file.Files.isSymbolicLink(Path path)
            if (!Files.isSymbolicLink(Paths.get(linkPath))){
                String targetPath="/nfs_mount/upload/"+path;
                FileUtils.createDir(targetPath);

                FileUtils.createSymbolicLink(linkPath,targetPath);
            }
            //디렉토리를 생성할 경우.
            //if (!Files.exists(fullpath)) {
            //    createDir();
            //}
            Files.copy(file.getInputStream(), Paths.get(linkPath).resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
    public Resource load(String filename) {
        try {
            Path file = Paths.get(uploadPath)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath)
                .toFile());
    }

    public List<Path> loadAll() {
        try {
            Path root = Paths.get(uploadPath);
            if (Files.exists(root)) {
                return Files.walk(root, 1)
                        .filter(path -> !path.equals(root))
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }
}
