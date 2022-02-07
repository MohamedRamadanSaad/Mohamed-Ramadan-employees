package com.sirma.assignment.services;

import com.sirma.assignment.handlers.CSVEmployeeDataHandler;
import com.sirma.assignment.models.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeService implements EmployeeServiceProcesses {
    private final CSVEmployeeDataHandler csvEmployeeDataHandler;

    public EmployeeService(final CSVEmployeeDataHandler csvEmployeeDataHandler) {
        this.csvEmployeeDataHandler = csvEmployeeDataHandler;
    }

    @Override
    public ResponseEntity<ResponseModel> findOverlapBetweenWorkingDates(MultipartFile file) throws Exception {
        ResponseModel responseModel = csvEmployeeDataHandler.doProcessTemplate(file);
        return ResponseEntity.ok(responseModel);
    }
}
