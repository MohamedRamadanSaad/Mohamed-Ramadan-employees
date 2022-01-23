package com.sirma.assignment.designPattern.template;

import com.sirma.assignment.models.CSVEmployeeRecord;
import com.sirma.assignment.models.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Component
public abstract class CSVProcessingTemplate {
    protected abstract boolean isValidateCSVFile(MultipartFile file);
    protected abstract List<CSVEmployeeRecord> readCSVData(MultipartFile file);
    protected abstract Map<String, List<CSVEmployeeRecord>> mapDataByProject(List<CSVEmployeeRecord> csvEmployeeRecords);
    protected abstract ResponseModel findResults(Map<String, List<CSVEmployeeRecord>> groupedByProjectMap);

    //template method
    public final ResponseModel doProcessTemplate(MultipartFile file) throws Exception {
        if(isValidateCSVFile(file)) {
            //read data from csv file
            List<CSVEmployeeRecord> records = readCSVData(file);
            // map data by project id
            Map<String, List<CSVEmployeeRecord>> groupedByProjectMap = mapDataByProject(records);
            //find working days overlap
            return findResults(groupedByProjectMap);
        }else {
            throw new Exception("Please select a valid CSV file.");
        }
    }
}
