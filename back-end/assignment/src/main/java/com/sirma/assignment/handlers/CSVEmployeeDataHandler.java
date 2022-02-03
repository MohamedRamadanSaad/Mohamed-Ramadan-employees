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
    public boolean isValidCSVFile(MultipartFile file) {
        loggerUtils.logInfoMessage(this.getClass(), "Validating uploaded file!");
        if (file.isEmpty() || !isCSVExtension(file)) {
            loggerUtils.logErrorMessage(this.getClass(), "Validation Failed!");
            return false;
        }
        loggerUtils.logInfoMessage(this.getClass(), "Validation Succeeded!");
        return true;
    }

    @Override
    public List<CSVEmployeeRecord> readCSVData(MultipartFile file) {
        loggerUtils.logInfoMessage(this.getClass(), "Start Reading CSV Data...");
        List<CSVEmployeeRecord> employee = null;
        try (final CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVEmployeeRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            // convert `CsvToBean` object to list of users
            employee = csvToBean.parse();
        } catch (final IOException e) {
            loggerUtils.logErrorMessage(this.getClass(), "CSV Reading Data Failed!");
            e.printStackTrace();
        }
        loggerUtils.logInfoMessage(this.getClass(), "CSV Reading Successfully!");
        return employee;
    }

    @Override
    public Map<String, List<CSVEmployeeRecord>> mapDataByProject(List<CSVEmployeeRecord> records) {
        loggerUtils.logInfoMessage(this.getClass(), "CSV Data Mapping Begin...");
        Map<String, List<CSVEmployeeRecord>> collect = records.stream().collect(groupingBy(CSVEmployeeRecord::getProjectId));
        loggerUtils.logInfoMessage(this.getClass(), "CSV Data Mapping Successfully!");
        return collect;
    }

    @Override
    public ResponseModel findResults(Map<String, List<CSVEmployeeRecord>> groupedByProjectMap) {
        loggerUtils.logInfoMessage(this.getClass(), "Start find results process!");
        ResultsDTO completeResultObject = new ResultsDTO();
        List<ResultRecordDTO> resultRecordDTOList = new ArrayList<>();
        groupedByProjectMap.forEach((project, employees) -> {
            loggerUtils.logInfoMessage(this.getClass(), "Project: ", project + " have employees > ", employees.toString());
            for (int i = 0; i < employees.size() - 1; i++) {
                CSVEmployeeRecord emp1 = employees.get(i);
                CSVEmployeeRecord emp2 = employees.get(i + 1);
                int overlap = findOverlapBetweenTwoDates(emp1.getDateFrom(), emp1.getDateTo(), emp2.getDateFrom(), emp2.getDateTo());
                if (overlap > 0) {
                    ResultRecordDTO resultRecordDTO = new ResultRecordDTO();
                    resultRecordDTO.setEmp1Id(emp1.getEmpId());
                    resultRecordDTO.setEmp2Id(emp2.getEmpId());
                    resultRecordDTO.setDays("" + overlap);
                    resultRecordDTO.setProjectId(project);
                    resultRecordDTOList.add(resultRecordDTO);
                }
            }
            completeResultObject.setResultRecordDTOS(resultRecordDTOList);
        });
        loggerUtils.logInfoMessage(this.getClass(), "End find results process successfully!");
        return ResponseModel.buildResponseModel(HttpStatus.OK, "CSV Processed Successfully!", completeResultObject);
    }

    public boolean isCSVExtension(MultipartFile file) {
        return FilenameUtils.getExtension(Objects.requireNonNull(file.getOriginalFilename())).toLowerCase().contains("csv");
    }

    private int findOverlapBetweenTwoDates(String emp1StartDate, String emp1EndDate, String emp2StartDate, String emp2EndDate) {

        /**
         * The Algorithm steps
         * 1- convert dates to localDate
         * 2- if any date is null put the current date instead of it
         *  Check for failed status and then go throw counting overlapping
         *      1- end date must be greater than end date if
         *      2- if emp1 leave the company before the emp2 so logically there is no overlap between them
         *      3- else then the employee have working together - calculate the interval:
         *          1- find interval between the max start date and the min end date
         *          2- plus one because the first day not included in days between
         */
        final LocalDate employee1LocalStartDate = correctDateAdapter(emp1StartDate);
        final LocalDate employee1LocalEndDate = !emp1EndDate.equalsIgnoreCase("null") ? correctDateAdapter(emp1EndDate) : LocalDate.now();
        final LocalDate employee2LocalStartDate = correctDateAdapter(emp2StartDate);
        final LocalDate employee2LocalEndDate = !emp2EndDate.equalsIgnoreCase("null") ? correctDateAdapter(emp2EndDate) : LocalDate.now();
        int numberOfOverlappingDays = 0;

        if (employee1LocalEndDate.isBefore(employee1LocalStartDate) || employee2LocalEndDate.isBefore(employee2LocalStartDate)) {
            loggerUtils.logInfoMessage(this.getClass(), "Not proper intervals");
        } else if (employee1LocalEndDate.isBefore(employee2LocalStartDate) || employee2LocalEndDate.isBefore(employee1LocalStartDate)) {
            // no overlap
            loggerUtils.logInfoMessage(this.getClass(), "One of employees leave the company before the other join.");
            return numberOfOverlappingDays;
        } else {
            final LocalDate laterStart = Collections.max(Arrays.asList(employee1LocalStartDate, employee2LocalStartDate));
            final LocalDate earlierEnd = Collections.min(Arrays.asList(employee1LocalEndDate, employee2LocalEndDate));
            numberOfOverlappingDays = (int) ChronoUnit.DAYS.between(laterStart, earlierEnd) + 1;
        }

        return numberOfOverlappingDays;
    }
}
