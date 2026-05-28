package org.example.salary.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Allowance {
    private String allowId;
    private String empId;
    private String overtimeType;
    private BigDecimal overtimeAmount;
    private BigDecimal allowanceRate;
    private BigDecimal allowanceAmount;
    private LocalDate overtimeDate;
    private String attMonth;

    // join fields
    private String empName;
    private String department;

    public String getAllowId() { return allowId; }
    public void setAllowId(String allowId) { this.allowId = allowId; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getOvertimeType() { return overtimeType; }
    public void setOvertimeType(String overtimeType) { this.overtimeType = overtimeType; }
    public BigDecimal getOvertimeAmount() { return overtimeAmount; }
    public void setOvertimeAmount(BigDecimal overtimeAmount) { this.overtimeAmount = overtimeAmount; }
    public BigDecimal getAllowanceRate() { return allowanceRate; }
    public void setAllowanceRate(BigDecimal allowanceRate) { this.allowanceRate = allowanceRate; }
    public BigDecimal getAllowanceAmount() { return allowanceAmount; }
    public void setAllowanceAmount(BigDecimal allowanceAmount) { this.allowanceAmount = allowanceAmount; }
    public LocalDate getOvertimeDate() { return overtimeDate; }
    public void setOvertimeDate(LocalDate overtimeDate) { this.overtimeDate = overtimeDate; }
    public String getAttMonth() { return attMonth; }
    public void setAttMonth(String attMonth) { this.attMonth = attMonth; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
