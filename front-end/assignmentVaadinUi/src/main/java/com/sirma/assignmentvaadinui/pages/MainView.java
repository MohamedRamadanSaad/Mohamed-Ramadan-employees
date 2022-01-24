package com.sirma.assignmentvaadinui.pages;

import com.sirma.assignmentvaadinui.backend.dto.ResultRecordDTO;
import com.sirma.assignmentvaadinui.backend.dto.ResultsDTO;
import com.sirma.assignmentvaadinui.backend.models.ResponseModel;
import com.sirma.assignmentvaadinui.backend.services.EmployeeService;
import com.sirma.assignmentvaadinui.backend.utils.LoggerUtils;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Comparator;

@Route(value = "")
@PageTitle("Employee Work Overlap | Sirma")
@Component
@UIScope
public class MainView extends VerticalLayout {


    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LoggerUtils loggerUtils;

    public MainView() {
        addClassName("list-view");
        Grid<ResultRecordDTO> grid = new Grid<>();
        setSizeFull();
        configureGrid(grid);
        add(getToolbar(grid), grid);
    }

    private void configureGrid(Grid<ResultRecordDTO> grid) {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.addColumn(ResultRecordDTO::getEmp1Id).setHeader("Employee ID #1");
        grid.addColumn(ResultRecordDTO::getEmp2Id).setHeader("Employee ID #2");
        grid.addColumn(ResultRecordDTO::getProjectId).setHeader("Project ID").setSortable(true);
        grid.addColumn(ResultRecordDTO::getDays).setHeader("Days worked").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setMultiSort(true);
    }

    private HorizontalLayout getToolbar(Grid<ResultRecordDTO> grid) {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload singleFileUpload = new Upload(memoryBuffer);
        singleFileUpload.setAcceptedFileTypes("text/csv", ".csv");
        singleFileUpload.addSucceededListener(event -> {
            loggerUtils.logInfoMessage(this.getClass(), "Start Upload file to backend...");
            // Get information about the uploaded file
            InputStream inputStream = memoryBuffer.getInputStream();
            // processFile(fileData, fileName, contentLength, mimeType);
            ResponseModel responseModel = employeeService.getUploadAndGetResultsDTO(inputStream);
            loggerUtils.logInfoMessage(this.getClass(), "End Upload file to backend...");
            loggerUtils.logInfoMessage(this.getClass(), "Getting results");

            ResultsDTO resultsDTO = responseModel.getResultsDTO();

            grid.setItems(resultsDTO.getResultRecordDTOS().stream().sorted(Comparator.comparingInt(ResultRecordDTO::getDays).reversed()));
        });
        add(singleFileUpload);
        HorizontalLayout toolbar = new HorizontalLayout(singleFileUpload);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}
