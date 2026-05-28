package org.example.salary.entity;

import java.math.BigDecimal;

public class Job {
    private String jobId;
    private String jobName;
    private String jobLevel;
    private BigDecimal baseSalary;
    private String remark;

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getJobLevel() { return jobLevel; }
    public void setJobLevel(String jobLevel) { this.jobLevel = jobLevel; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
