package org.example.salary.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class YearlyBonus {
    private String bonusId;
    private String empId;
    private Integer year;
    private BigDecimal totalSalary;
    private BigDecimal totalAllowance;
    private BigDecimal bonusAmount;
    private LocalDateTime calcTime;
    private String operator;

    // join fields
    private String empName;
    private String department;

    public String getBonusId() { return bonusId; }
    public void setBonusId(String bonusId) { this.bonusId = bonusId; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getTotalSalary() { return totalSalary; }
    public void setTotalSalary(BigDecimal totalSalary) { this.totalSalary = totalSalary; }
    public BigDecimal getTotalAllowance() { return totalAllowance; }
    public void setTotalAllowance(BigDecimal totalAllowance) { this.totalAllowance = totalAllowance; }
    public BigDecimal getBonusAmount() { return bonusAmount; }
    public void setBonusAmount(BigDecimal bonusAmount) { this.bonusAmount = bonusAmount; }
    public LocalDateTime getCalcTime() { return calcTime; }
    public void setCalcTime(LocalDateTime calcTime) { this.calcTime = calcTime; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
