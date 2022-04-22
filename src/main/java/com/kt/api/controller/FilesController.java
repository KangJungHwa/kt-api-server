package com.kt.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.kt.api.model.file.FileData;
import com.kt.api.model.file.UploadResponseMessage;
import com.kt.api.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

/**
 * 아래 참고
 * https://frontbackend.com/spring-boot/spring-boot-upload-file-to-filesystem
 */
@RestController
@RequestMapping("files")
public class FilesController {

    private final FileService fileService;

    @Autowired
    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * path 입력시 쌍따온표 입력하면 에러남.
     * curl --request POST \
     *   --url http://localhost:8080/files/upload \
     *   --header 'Content-Type: multipart/form-data; boundary=---011000010111000001101001' \
     *   --form 'file=@C:\DEV\values.yaml' \
     *   --form path=kang2_upload \
     *   --form isOverWrite=false
     * @param file
     * @param path
     * @param isOverWrite
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<UploadResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                            @RequestParam("path") String path,
                                                            @RequestParam("isOverWrite") boolean isOverWrite
                                                            ) {
        try {
            System.out.println("path :~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ "+path);
            System.out.println("isOverWrite :~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ "+isOverWrite);
            fileService.save(file,path);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new UploadResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new UploadResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
        }
    }

    @GetMapping
    public ResponseEntity<List<FileData>> getListFiles() {
        List<FileData> fileInfos = fileService.loadAll()
                .stream()
                .map(this::pathToFileData)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @DeleteMapping
    public void delete() {
        fileService.deleteAll();
    }

    private FileData pathToFileData(Path path) {
        FileData fileData = new FileData();
        String filename = path.getFileName()
                .toString();
        fileData.setFilename(filename);
        fileData.setUrl(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", filename)
                .build()
                .toString());
        try {
            fileData.setSize(Files.size(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return fileData;
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
