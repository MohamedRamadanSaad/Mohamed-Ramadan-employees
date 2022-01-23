package com.sirma.assignment.services;

import com.sirma.assignment.handlers.CSVEmployeeDataHandler;
import com.sirma.assignment.models.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeService {

    private CSVEmployeeDataHandler csvEmployeeDataHandler;
    public EmployeeService(final CSVEmployeeDataHandler csvEmployeeDataHandler){
        this.csvEmployeeDataHandler= csvEmployeeDataHandler;
    }

    public ResponseEntity<ResponseModel> findOverlapBetweenWorkingDates(MultipartFile file){
        ResponseModel responseModel = null;
        try {
            responseModel = csvEmployeeDataHandler.doProcessTemplate(file);
        }catch (Exception ex){
            return ResponseEntity.badRequest().
                    body(ResponseModel.buildResponseModel(HttpStatus.BAD_REQUEST,ex.getMessage()));
        }
        return ResponseEntity.ok(responseModel);
    }
}
