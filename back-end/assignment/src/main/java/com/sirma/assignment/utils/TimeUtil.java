package com.sirma.assignment.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Component
public class TimeUtil {

    private static final DateTimeFormatter parserOptionalFormats;
    static {
        parserOptionalFormats = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendOptional(DateTimeFormatter.ofPattern("d MMM uuuu"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyyMMdd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("MM/DD/YY"))
                .appendOptional(DateTimeFormatter.ofPattern("DD/MM/YY"))
                .appendOptional(DateTimeFormatter.ofPattern("YY/MM/DD"))
                //.appendOptional(DateTimeFormatter.ofPattern("Month D, Yr"))
                .appendOptional(DateTimeFormatter.ofPattern("M/D/YY"))
                .appendOptional(DateTimeFormatter.ofPattern("D/M/YY"))
                .appendOptional(DateTimeFormatter.ofPattern("YY/M/D"))
                //.appendOptional(DateTimeFormatter.ofPattern("bM/bD/YY"))
               // .appendOptional(DateTimeFormatter.ofPattern("bD/bM/YY"))
                //.appendOptional(DateTimeFormatter.ofPattern("YY/bM/bD"))
                .appendOptional(DateTimeFormatter.ofPattern("MMDDYY"))
                .appendOptional(DateTimeFormatter.ofPattern("DDMMYY"))
                .appendOptional(DateTimeFormatter.ofPattern("YYMMDD"))
                .appendOptional(DateTimeFormatter.ofPattern("MMDDYY"))
                .appendOptional(DateTimeFormatter.ofPattern("DDMMYY"))
                .appendOptional(DateTimeFormatter.ofPattern("YYMMDD"))
                .appendOptional(DateTimeFormatter.ofPattern("day/YY"))
                .appendOptional(DateTimeFormatter.ofPattern("YY/day"))
               // .appendOptional(DateTimeFormatter.ofPattern("D Month, Yr"))
                .appendOptional(DateTimeFormatter.ofPattern("YY, MM D"))
                .appendOptional(DateTimeFormatter.ofPattern("MM-DD-YYYY"))
                .appendOptional(DateTimeFormatter.ofPattern("DD-MM-YYYY"))
                .appendOptional(DateTimeFormatter.ofPattern("YYYYY-MM-DD"))
                .appendOptional(DateTimeFormatter.ofPattern("MM DD, YYYY"))
                .appendOptional(DateTimeFormatter.ofPattern("DD MM, YYYY"))
                .appendOptional(DateTimeFormatter.ofPattern("YYYY, MM DD"))
                .toFormatter();
    }
    public static LocalDate correctDateAdapter(String dateString){
        return LocalDate.parse(dateString, parserOptionalFormats);
    }
}
