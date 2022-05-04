package com.kt.api.util;

import com.kt.api.model.DefaultResponse;
import com.kt.api.model.StatusEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;

public class ResponseUtils {
    public static ResponseEntity<DefaultResponse> getResponse(StatusEnum statusEnum,String message,Object data) {

        DefaultResponse response = new DefaultResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        response.setStatus(statusEnum);
        response.setMessage(message);
        response.setData(data);
        HttpStatus httpStatus=null;
        switch (statusEnum){
            case OK :
                httpStatus = HttpStatus.OK;
                break;
            case BAD_REQUEST :
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case NOT_FOUND :
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            case INTERNAL_SERVER_ERROR:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                httpStatus = HttpStatus.EXPECTATION_FAILED;
        }

        return new ResponseEntity<>(response, headers, httpStatus);
    }
}
