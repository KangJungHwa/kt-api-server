package com.kt.api.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.kt.api.model.DefaultResponse;
import com.kt.api.model.ExecuteResult;
import com.kt.api.model.MQRequest;
import com.kt.api.model.StatusEnum;
import com.kt.api.model.file.FileData;
import com.kt.api.model.file.UploadResponseMessage;
import com.kt.api.service.FileService;
import com.kt.api.util.FileUtils;
import com.kt.api.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;

/**
 * 아래 참고
 * https://frontbackend.com/spring-boot/spring-boot-upload-file-to-filesystem
 */
@Slf4j
@RestController
@RequestMapping("files")
public class FilesController {

    @Autowired
    FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

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
    public ResponseEntity<DefaultResponse> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("path") String path,
                                                      @RequestParam("isOverWrite") boolean isOverWrite )
    {
        try {
            fileService.save(file,path);
        } catch (Exception e) {
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,"File upload fail!","File upload fail!");
        }
        return ResponseUtils.getResponse(StatusEnum.OK,"File upload success!","File upload success!");
    }



    /**
     * curl --request GET \
     *   --url http://service-api.k8s.io:30083/files/listfile/upload_test
     * @param fileData
     * @return
     */
    @GetMapping("/listfile")
    @ResponseBody
    public ResponseEntity<DefaultResponse> listFile(@RequestBody @Valid FileData fileData) {
        log.info("{}", String.format("'%s' 파일 리스트를 표시합니다..", uploadPath+fileData.getPath()));
        File [] files=null;
        List<FileData> listFile=null;
        try{
            File filepath = new File(uploadPath+fileData.getPath());
                listFile = (List<FileData>)FileUtils.listFile(uploadPath+fileData.getPath()).getResult();
        }catch(Exception e){
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,"File list search fail!","List file fail!");
        }
        return ResponseUtils.getResponse(StatusEnum.OK,"File list search Success!",listFile);
    }

    /**
     * curl --request DELETE \
     *   --url http://localhost:8080/files/delfile \
     *   --header 'Content-Type: application/json' \
     *   --data '{
     * 	"path":"C:\\\\service-api\\upload\\upload_test"
     * }'
     * 파일 삭제
     * 파라메터가 DIRECTORY면 DIRECTORY 삭제 FILE이면 FILE 삭제
     * @param fileData
     * @return
     */
    @DeleteMapping("/delfile")
    @ResponseBody
    public ResponseEntity<DefaultResponse> delete(@RequestBody @Valid FileData fileData) {
        ExecuteResult executeResult = FileUtils.delete(uploadPath+fileData.getPath());
        //ExecuteResult executeResult = FileUtils.delete(fileData.getPath());
        if(executeResult.isStatus()){
            return ResponseUtils.getResponse(StatusEnum.OK,
                    executeResult.getResult().toString(),executeResult.getResult().toString());
        }else{
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,
                    executeResult.getResult().toString(),executeResult.getResult().toString());
        }
    }
    /**
     * curl --request GET \
     *   --url http://localhost:8080/files/readfile \
     *   --header 'Content-Type: application/json' \
     *   --data '{
     * 	"path": "C:\\\\service-api\\upload\\upload_test\\1.txt"
     * }'
     * 파일 읽기
     * @param fileData
     * @return
     */
    @GetMapping("/readfile")
    @ResponseBody
    public ResponseEntity<DefaultResponse> readfile(@RequestBody @Valid FileData fileData) {
        //ExecuteResult executeResult = FileUtils.readFile(uploadPath+fileData.getPath());
        ExecuteResult executeResult = FileUtils.readFile(fileData.getPath());
        if(executeResult.isStatus()){
            return ResponseUtils.getResponse(StatusEnum.OK,
                    fileData.getPath()+" file read success!",executeResult.getResult().toString());
        }else{
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,
                    executeResult.getResult().toString(),executeResult.getResult().toString());
        }
    }
}
