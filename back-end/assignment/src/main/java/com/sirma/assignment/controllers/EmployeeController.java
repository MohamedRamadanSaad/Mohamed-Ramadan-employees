package com.sirma.assignment.controllers;

import com.sirma.assignment.models.ResponseModel;
import com.sirma.assignment.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("upload-csv-file")
    public ResponseEntity<ResponseModel> findEmployeeWorkingOverlap(@RequestParam("file") MultipartFile file) {
        try {
            return employeeService.findOverlapBetweenWorkingDates(file);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().
                    body(ResponseModel.buildResponseModel(HttpStatus.BAD_REQUEST, ex.getMessage()));
        }
    }

}
