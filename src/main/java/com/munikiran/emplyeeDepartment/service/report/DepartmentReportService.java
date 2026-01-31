package com.munikiran.emplyeeDepartment.service.report;

import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DepartmentReportService {

    private final DepartmentRepository departmentRepository;

    public byte[] generateDepartmentEmployeeReport() {

        try {
            List<Department> departments = departmentRepository.findAll();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            InputStream reportStream =
                    new ClassPathResource("reports/department_employees.jrxml").getInputStream();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            JasperPrint finalPrint = new JasperPrint();
            finalPrint.setPageWidth(595);
            finalPrint.setPageHeight(842);

            for (Department dept : departments) {

                Map<String, Object> params = new HashMap<>();
                params.put("departmentName", dept.getName());
                params.put("departmentLocation", dept.getLocation());

                JRBeanCollectionDataSource dataSource =
                        new JRBeanCollectionDataSource(dept.getEmployees());

                JasperPrint departmentPrint =
                        JasperFillManager.fillReport(jasperReport, params, dataSource);

                departmentPrint.getPages().forEach(finalPrint::addPage);
            }

            JasperExportManager.exportReportToPdfStream(finalPrint, outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating department report", e);
        }
    }
}
