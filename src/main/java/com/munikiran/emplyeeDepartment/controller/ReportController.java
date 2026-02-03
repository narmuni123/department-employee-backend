package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.common.ApiResponse;
import com.munikiran.emplyeeDepartment.common.constants.SuccessMessages;
import com.munikiran.emplyeeDepartment.service.report.DepartmentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final DepartmentReportService reportService;

    @GetMapping("/departments/employees")
    public ResponseEntity<ApiResponse<byte[]>> downloadDepartmentEmployeeReport() {

        byte[] pdf = reportService.generateDepartmentEmployeeReport();
        
        ApiResponse<byte[]> response = ApiResponse.success(
            SuccessMessages.REPORT_GENERATED,
            pdf
        );
        
        return ResponseEntity.ok(response);
    }
}
