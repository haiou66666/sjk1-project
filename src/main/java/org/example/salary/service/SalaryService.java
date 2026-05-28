package org.example.salary.service;

import org.example.salary.entity.*;
import org.example.salary.dao.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SalaryService {
    private final EmployeeDao employeeDao;
    private final JobDao jobDao;
    private final AttendanceDao attendanceDao;
    private final AllowanceDao allowanceDao;
    private final MonthlySalaryDao monthlySalaryDao;
    private final YearlyBonusDao yearlyBonusDao;

    public SalaryService(EmployeeDao employeeDao, JobDao jobDao, AttendanceDao attendanceDao,
                         AllowanceDao allowanceDao, MonthlySalaryDao monthlySalaryDao, YearlyBonusDao yearlyBonusDao) {
        this.employeeDao = employeeDao; this.jobDao = jobDao; this.attendanceDao = attendanceDao;
        this.allowanceDao = allowanceDao; this.monthlySalaryDao = monthlySalaryDao; this.yearlyBonusDao = yearlyBonusDao;
    }

    @Transactional
    public Map<String, Object> generateMonthlySalary(String month, String operator) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Employee> employees = employeeDao.findAll();
        int success = 0, fail = 0;
        List<String> errors = new ArrayList<>();
        for (Employee emp : employees) {
            if (!"在职".equals(emp.getStatus())) continue;
            try {
                Job job = jobDao.findById(emp.getJobId());
                if (job == null) { errors.add(emp.getEmpName() + " no job"); fail++; continue; }
                BigDecimal baseSalary = job.getBaseSalary();
                BigDecimal allowanceTotal = allowanceDao.sumByEmpMonth(emp.getEmpId(), month);
                BigDecimal deduction = BigDecimal.ZERO;
                List<Attendance> atts = attendanceDao.search(emp.getEmpId(), null, month);
                if (!atts.isEmpty() && atts.get(0).getLeaveDays() != null && atts.get(0).getLeaveDays().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal dailySalary = baseSalary.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP);
                    deduction = atts.get(0).getLeaveDays().multiply(dailySalary).setScale(2, RoundingMode.HALF_UP);
                }
                BigDecimal netSalary = baseSalary.add(allowanceTotal).subtract(deduction);
                String salaryId = "SAL-" + month + "-" + emp.getEmpId();
                MonthlySalary ms = new MonthlySalary();
                ms.setSalaryId(salaryId); ms.setEmpId(emp.getEmpId()); ms.setSalaryMonth(month);
                ms.setBaseSalary(baseSalary); ms.setAllowanceTotal(allowanceTotal);
                ms.setDeduction(deduction); ms.setNetSalary(netSalary); ms.setOperator(operator);
                if (monthlySalaryDao.exists(emp.getEmpId(), month) > 0) monthlySalaryDao.update(ms);
                else monthlySalaryDao.insert(ms);
                success++;
            } catch (Exception e) { errors.add(emp.getEmpName() + ": " + e.getMessage()); fail++; }
        }
        result.put("success", success); result.put("fail", fail); result.put("errors", errors);
        result.put("month", month); result.put("total", success + fail);
        return result;
    }

    @Transactional
    public Map<String, Object> generateYearlyBonus(int year, String operator) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Employee> employees = employeeDao.findAll();
        int success = 0;
        List<String> errors = new ArrayList<>();
        for (Employee emp : employees) {
            if (!"在职".equals(emp.getStatus())) continue;
            try {
                BigDecimal totalSalary = monthlySalaryDao.sumYearSalary(emp.getEmpId(), year);
                BigDecimal totalAllowance = allowanceDao.sumByEmpYear(emp.getEmpId(), year);
                BigDecimal bonusAmount = totalSalary.add(totalAllowance).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
                YearlyBonus yb = new YearlyBonus();
                yb.setBonusId("BONUS-" + year + "-" + emp.getEmpId()); yb.setEmpId(emp.getEmpId());
                yb.setYear(year); yb.setTotalSalary(totalSalary); yb.setTotalAllowance(totalAllowance);
                yb.setBonusAmount(bonusAmount); yb.setOperator(operator);
                yearlyBonusDao.insertOrUpdate(yb);
                success++;
            } catch (Exception e) { errors.add(emp.getEmpName() + ": " + e.getMessage()); }
        }
        result.put("success", success); result.put("errors", errors); result.put("year", year);
        return result;
    }

    public List<MonthlySalary> findAllSalaries() { return monthlySalaryDao.findAllWithEmp(); }
    public List<MonthlySalary> searchSalaries(String empId, String empName, String salaryMonth, Integer year) {
        return monthlySalaryDao.search(empId, empName, salaryMonth, year);
    }
    public List<MonthlySalary> findByDeptMonth(String department, String salaryMonth) {
        return monthlySalaryDao.findByDeptMonth(department, salaryMonth);
    }
    public List<YearlyBonus> findAllBonuses() { return yearlyBonusDao.findAllWithEmp(); }
    public List<YearlyBonus> searchBonuses(String empId, String department, Integer year) {
        return yearlyBonusDao.search(empId, department, year);
    }
}
