package org.example.salary.service;

import org.example.salary.entity.*;
import org.example.salary.dao.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class StatisticsService {
    private final MonthlySalaryDao monthlySalaryDao;
    private final EmployeeDao employeeDao;

    public StatisticsService(MonthlySalaryDao monthlySalaryDao, EmployeeDao employeeDao) {
        this.monthlySalaryDao = monthlySalaryDao; this.employeeDao = employeeDao;
    }

    public Map<String, Object> deptMonthStats(String department, String month) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<MonthlySalary> salaries = monthlySalaryDao.findByDeptMonth(department, month);
        BigDecimal totalSalary = BigDecimal.ZERO;
        for (MonthlySalary ms : salaries) totalSalary = totalSalary.add(ms.getNetSalary());
        int count = employeeDao.countActiveByDept(department);
        BigDecimal avgSalary = count > 0 ? totalSalary.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        result.put("department", department); result.put("month", month); result.put("salaries", salaries);
        result.put("totalSalary", totalSalary); result.put("avgSalary", avgSalary); result.put("employeeCount", count);
        return result;
    }

    public Map<String, Object> monthStats(String month) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<MonthlySalary> all = monthlySalaryDao.search(null, null, month, null);
        Map<String, List<MonthlySalary>> byDept = new LinkedHashMap<>();
        for (MonthlySalary ms : all) byDept.computeIfAbsent(ms.getDepartment(), k -> new ArrayList<>()).add(ms);
        List<Map<String, Object>> deptStats = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO; int totalEmployees = 0;
        for (Map.Entry<String, List<MonthlySalary>> entry : byDept.entrySet()) {
            BigDecimal deptTotal = BigDecimal.ZERO;
            for (MonthlySalary ms : entry.getValue()) deptTotal = deptTotal.add(ms.getNetSalary());
            int deptCount = entry.getValue().size();
            BigDecimal deptAvg = deptCount > 0 ? deptTotal.divide(new BigDecimal(deptCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            Map<String, Object> ds = new LinkedHashMap<>();
            ds.put("department", entry.getKey()); ds.put("total", deptTotal); ds.put("average", deptAvg); ds.put("count", deptCount);
            deptStats.add(ds);
            grandTotal = grandTotal.add(deptTotal); totalEmployees += deptCount;
        }
        BigDecimal overallAvg = totalEmployees > 0 ? grandTotal.divide(new BigDecimal(totalEmployees), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        result.put("month", month); result.put("deptStats", deptStats); result.put("grandTotal", grandTotal);
        result.put("overallAvg", overallAvg); result.put("totalEmployees", totalEmployees);
        return result;
    }
}
