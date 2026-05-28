package org.example.salary.entity;

import java.math.BigDecimal;

public class Attendance {
    private String attId;
    private String empId;
    private String attMonth;
    private Integer workDays;
    private BigDecimal leaveDays;
    private BigDecimal overtimeHours;
    private BigDecimal overtimeDays;
    private String attRemark;

    // join fields
    private String empName;
    private String department;

    public String getAttId() { return attId; }
    public void setAttId(String attId) { this.attId = attId; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getAttMonth() { return attMonth; }
    public void setAttMonth(String attMonth) { this.attMonth = attMonth; }
    public Integer getWorkDays() { return workDays; }
    public void setWorkDays(Integer workDays) { this.workDays = workDays; }
    public BigDecimal getLeaveDays() { return leaveDays; }
    public void setLeaveDays(BigDecimal leaveDays) { this.leaveDays = leaveDays; }
    public BigDecimal getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(BigDecimal overtimeHours) { this.overtimeHours = overtimeHours; }
    public BigDecimal getOvertimeDays() { return overtimeDays; }
    public void setOvertimeDays(BigDecimal overtimeDays) { this.overtimeDays = overtimeDays; }
    public String getAttRemark() { return attRemark; }
    public void setAttRemark(String attRemark) { this.attRemark = attRemark; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
