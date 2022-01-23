package com.sirma.assignment.models;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CSVEmployeeRecord {
    @CsvBindByPosition(position = 0)
    private String empId;
    @CsvBindByPosition(position = 1)
    private String projectId;
    @CsvBindByPosition(position = 2)
    private String dateFrom;
    @CsvBindByPosition(position = 3)
    private String DateTo;
}
