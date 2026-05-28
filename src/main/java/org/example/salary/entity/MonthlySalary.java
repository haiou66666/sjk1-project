package org.example.salary.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MonthlySalary {
    private String salaryId;
    private String empId;
    private String salaryMonth;
    private BigDecimal baseSalary;
    private BigDecimal allowanceTotal;
    private BigDecimal deduction;
    private BigDecimal netSalary;
    private LocalDateTime calcTime;
    private String operator;

    // join fields
    private String empName;
    private String department;
    private String jobName;

    public String getSalaryId() { return salaryId; }
    public void setSalaryId(String salaryId) { this.salaryId = salaryId; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getSalaryMonth() { return salaryMonth; }
    public void setSalaryMonth(String salaryMonth) { this.salaryMonth = salaryMonth; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getAllowanceTotal() { return allowanceTotal; }
    public void setAllowanceTotal(BigDecimal allowanceTotal) { this.allowanceTotal = allowanceTotal; }
    public BigDecimal getDeduction() { return deduction; }
    public void setDeduction(BigDecimal deduction) { this.deduction = deduction; }
    public BigDecimal getNetSalary() { return netSalary; }
    public void setNetSalary(BigDecimal netSalary) { this.netSalary = netSalary; }
    public LocalDateTime getCalcTime() { return calcTime; }
    public void setCalcTime(LocalDateTime calcTime) { this.calcTime = calcTime; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
}
