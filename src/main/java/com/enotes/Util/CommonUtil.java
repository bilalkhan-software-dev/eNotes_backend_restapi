package com.enotes.Util;

import com.enotes.Handler.GenericResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class CommonUtil {


    @Value("${website.base.url}")
    private String WEBSITE_DOMAIN;

    public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus httpStatus){

        GenericResponse response = GenericResponse.builder()
                .responseHttpStatus(httpStatus)
                .status("Success")
                .message("Fetching data ..... ")
                .data(data)
                .build();

        return response.create();
    }

    public static ResponseEntity<?> createBuildResponseMessage(String message,HttpStatus httpStatus){

        GenericResponse response = GenericResponse.builder()
                .responseHttpStatus(httpStatus)
                .status("Success")
                .message(message)
                .build();

        return response.create();
    }
    public static ResponseEntity<?> createErrorResponse(Object data,HttpStatus httpStatus){

        GenericResponse response = GenericResponse.builder()
                .responseHttpStatus(httpStatus)
                .status("Failed !").message("Something went wrong !")
                .data(data)
                .build();

        return response.create();
    }
    public static ResponseEntity<?> createErrorResponseMessage(String message,HttpStatus httpStatus){

        GenericResponse response = GenericResponse.builder()
                .responseHttpStatus(httpStatus)
                .status("Failed !")
                .message(message)
                .build();

        return response.create();
    }

    public static String getContentType(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName).toLowerCase(); // Normalize case

        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "txt" -> "text/plain";
            case "png" -> "image/png";
            case "jpeg", "jpg" -> "image/jpeg";
            default -> "application/octet-stream";
        };
    }


    public String generateUrl(Integer userId,String token){
        return WEBSITE_DOMAIN+"/api/v1/home/verify-email?userId=" +userId+ "&token=" +token;
    }

}
