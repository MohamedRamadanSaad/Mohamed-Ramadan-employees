package com.sirma.assignment.handlers;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sirma.assignment.designPattern.template.CSVProcessingTemplate;
import com.sirma.assignment.dto.ResultRecordDTO;
import com.sirma.assignment.dto.ResultsDTO;
import com.sirma.assignment.models.CSVEmployeeRecord;
import com.sirma.assignment.models.ResponseModel;
import com.sirma.assignment.utils.LoggerUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.sirma.assignment.utils.TimeUtil.correctDateAdapter;
import static java.util.stream.Collectors.groupingBy;

@Component
public class CSVEmployeeDataHandler extends CSVProcessingTemplate {

    @Autowired
    private LoggerUtils loggerUtils;

    @Override
    public boolean isValidateCSVFile(MultipartFile file) {
        loggerUtils.logInfoMessage(this.getClass(),"Validating uploaded file!");
        if (file.isEmpty() || !isCSVExtension(file)) {
            loggerUtils.logErrorMessage(this.getClass(),"Validation Failed!");
            return false;
        }
        loggerUtils.logInfoMessage(this.getClass(),"Validation Succeeded!");
        return true;
    }

    @Override
    public List<CSVEmployeeRecord> readCSVData(MultipartFile file) {
        loggerUtils.logInfoMessage(this.getClass(),"Start Reading CSV Data...");
        List<CSVEmployeeRecord> employee = null;
        try (final CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVEmployeeRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            // convert `CsvToBean` object to list of users
            employee = csvToBean.parse();
        } catch (final IOException e) {
            loggerUtils.logErrorMessage(this.getClass(),"CSV Reading Data Failed!");
            e.printStackTrace();
        }
        loggerUtils.logInfoMessage(this.getClass(),"CSV Reading Successfully!");
        return employee;
    }

    @Override
    public Map<String, List<CSVEmployeeRecord>> mapDataByProject(List<CSVEmployeeRecord> records) {
        loggerUtils.logInfoMessage(this.getClass(),"CSV Data Mapping Begin...");
        Map<String, List<CSVEmployeeRecord>> collect = records.stream().collect(groupingBy(CSVEmployeeRecord::getProjectId));
        loggerUtils.logInfoMessage(this.getClass(),"CSV Data Mapping Successfully!");
        return collect;
    }

    @Override
    public ResponseModel findResults(Map<String, List<CSVEmployeeRecord>> groupedByProjectMap) {
        loggerUtils.logInfoMessage(this.getClass(),"Start find results process!");
        ResultsDTO completeResultObject = new ResultsDTO();
        List<ResultRecordDTO> resultRecordDTOS = new ArrayList<>();
        groupedByProjectMap.forEach((project,employees)->{
            loggerUtils.logInfoMessage(this.getClass(),"Project: ",project+" have employees > ",employees.toString());
            for(int i=0 ; i<employees.size()-1; i++){
                CSVEmployeeRecord emp1 = employees.get(i);
                CSVEmployeeRecord emp2 = employees.get(i+1);
                int overlap = findOverlapBetweenTwoDates(emp1.getDateFrom(),emp1.getDateTo(),emp2.getDateFrom(),emp2.getDateTo());
                if(overlap > 0 ){
                    ResultRecordDTO resultRecordDTO = new ResultRecordDTO();
                    resultRecordDTO.setEmp1Id(emp1.getEmpId());
                    resultRecordDTO.setEmp2Id(emp2.getEmpId());
                    resultRecordDTO.setDays(""+overlap);
                    resultRecordDTO.setProjectId(project);
                    resultRecordDTOS.add(resultRecordDTO);
                }
            }
            completeResultObject.setResultRecordDTOS(resultRecordDTOS);
        });
        loggerUtils.logInfoMessage(this.getClass(),"End find results process successfully!");
        return ResponseModel.buildResponseModel(HttpStatus.OK,"CSV Processed Successfully!",completeResultObject);
    }

    public boolean isCSVExtension(MultipartFile file){
        return FilenameUtils.getExtension(Objects.requireNonNull(file.getOriginalFilename())).toLowerCase().contains("csv");
    }

    private   int findOverlapBetweenTwoDates(String emp1StartDate, String emp1EndDate, String emp2StartDate, String emp2EndDate) {

        final String emp1StartStr = emp1StartDate;
        final String emp1EndStr = emp1EndDate;
        final String emp2StartStr = emp2StartDate;
        final String emp2EndStr = emp2EndDate;

        final LocalDate i1Start = correctDateAdapter(emp1StartStr);
        final LocalDate i1End = !emp1EndStr.equalsIgnoreCase("null")?correctDateAdapter(emp1EndStr):LocalDate.now();
        final LocalDate i2Start =correctDateAdapter(emp2StartStr);
        final LocalDate i2End = !emp2EndStr.equalsIgnoreCase("null")?correctDateAdapter(emp2EndStr):LocalDate.now();
        int numberOfOverlappingDays = 0;

        if (i1End.isBefore(i1Start) || i2End.isBefore(i2Start)) {
            loggerUtils.logInfoMessage(this.getClass(),"Not proper intervals");
        } else {
            if (i1End.isBefore(i2Start) || i2End.isBefore(i1Start)) {
                // no overlap
                numberOfOverlappingDays = 0;
            } else {
                final LocalDate laterStart = Collections.max(Arrays.asList(i1Start, i2Start));
                final LocalDate earlierEnd = Collections.min(Arrays.asList(i1End, i2End));
                numberOfOverlappingDays = (int) ChronoUnit.DAYS.between(laterStart, earlierEnd) +1;
            }
        }
        return numberOfOverlappingDays;
    }
}
