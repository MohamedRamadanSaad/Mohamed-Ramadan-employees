package com.sirma.assignmentvaadinui.backend.services;

import com.sirma.assignmentvaadinui.backend.models.ResponseModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class EmployeeService {

    @Value("${values.upload-url}")
    private String uploadURL;

    private final WebClient webClient = WebClient.builder().build();

    public ResponseModel getUploadAndGetResultsDTO(InputStream inputStream) {
        return this.webClient.post().uri(uploadURL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(fromFile(inputStream)))
                .retrieve()
                .bodyToMono(ResponseModel.class)
                .block();
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(InputStream inputStream) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            File tempFile = File.createTempFile("sirma", ".csv");
            FileUtils.writeByteArrayToFile(tempFile, bytes);
            builder.part("file",  new FileSystemResource(tempFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

}
