package org.example.salary.controller;

import org.example.salary.service.SalaryService;
import org.example.salary.service.StatisticsService;
import org.example.salary.entity.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    private final SalaryService ss;
    private final StatisticsService sts;
    public SalaryController(SalaryService ss, StatisticsService sts) { this.ss = ss; this.sts = sts; }

    @GetMapping("/monthly")
    public List<MonthlySalary> monthlyList(@RequestParam(required = false) String empId,
                                            @RequestParam(required = false) String empName,
                                            @RequestParam(required = false) String salaryMonth,
                                            @RequestParam(required = false) Integer year) {
        return ss.searchSalaries(empId, empName, salaryMonth, year);
    }

    @PostMapping("/generate")
    public Map<String,Object> generate(@RequestBody Map<String,String> params) {
        return ss.generateMonthlySalary(params.get("month"), params.getOrDefault("operator", "SYSTEM"));
    }

    @GetMapping("/bonus")
    public List<YearlyBonus> bonusList(@RequestParam(required = false) String empId,
                                        @RequestParam(required = false) String department,
                                        @RequestParam(required = false) Integer year) {
        return ss.searchBonuses(empId, department, year);
    }

    @PostMapping("/bonus/generate")
    public Map<String,Object> bonusGenerate(@RequestBody Map<String,Object> params) {
        int year = Integer.parseInt(params.get("year").toString());
        String op = params.getOrDefault("operator", "SYSTEM").toString();
        return ss.generateYearlyBonus(year, op);
    }

    @GetMapping("/dept")
    public Map<String,Object> deptQuery(@RequestParam String department, @RequestParam String salaryMonth) {
        return sts.deptMonthStats(department, salaryMonth);
    }

    @GetMapping("/stats")
    public Map<String,Object> monthStats(@RequestParam String month) {
        return sts.monthStats(month);
    }
}