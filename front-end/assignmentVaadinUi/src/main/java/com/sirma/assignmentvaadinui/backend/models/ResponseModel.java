package com.sirma.assignmentvaadinui.backend.models;


import com.sirma.assignmentvaadinui.backend.dto.ResultsDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseModel {
    private Integer httpStatus;
    private String message;
    private ResultsDTO resultsDTO;


    public static ResponseModel buildResponseModel(HttpStatus httpStatus, String message){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setHttpStatus(httpStatus.value());
        responseModel.setMessage(message);
        return responseModel;
    }

    public static ResponseModel  buildResponseModel(HttpStatus httpStatus, String message, ResultsDTO resultsDTO){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setHttpStatus(httpStatus.value());
        responseModel.setMessage(message);
        responseModel.setResultsDTO(resultsDTO);
        return responseModel;
    }
}