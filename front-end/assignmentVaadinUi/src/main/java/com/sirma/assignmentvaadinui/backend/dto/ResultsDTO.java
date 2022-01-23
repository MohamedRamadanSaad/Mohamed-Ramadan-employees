package com.sirma.assignmentvaadinui.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ResultsDTO {
    private List<ResultRecordDTO> resultRecordDTOS;
}

