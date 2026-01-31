package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.service.report.DepartmentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final DepartmentReportService reportService;

    @GetMapping("/departments/employees")
    public ResponseEntity<byte[]> downloadDepartmentEmployeeReport() {

        byte[] pdf = reportService.generateDepartmentEmployeeReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=department-employees.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
