package com.sirma.assignment.services;

import com.sirma.assignment.models.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeServiceProcesses {
    ResponseEntity<ResponseModel> findOverlapBetweenWorkingDates(MultipartFile file) throws Exception;
}
