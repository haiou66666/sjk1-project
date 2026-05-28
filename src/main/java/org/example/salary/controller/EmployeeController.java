package org.example.salary.controller;

import org.example.salary.entity.Employee;
import org.example.salary.service.EmployeeService;
import org.example.salary.service.JobService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService es;
    private final JobService js;
    public EmployeeController(EmployeeService es, JobService js) { this.es = es; this.js = js; }

    @GetMapping
    public List<Employee> list(@RequestParam(required = false) String keyword) { return es.search(keyword); }
    @GetMapping("/{id}")
    public Employee get(@PathVariable String id) { return es.findById(id); }
    @PostMapping
    public Map<String,Object> save(@RequestBody Employee emp) {
        if (emp.getStatus() == null || emp.getStatus().isEmpty()) emp.setStatus("在职");
        es.save(emp);
        return Map.of("success", true, "msg", "保存成功");
    }
    @DeleteMapping("/{id}")
    public Map<String,Object> delete(@PathVariable String id) { es.delete(id); return Map.of("success", true); }
    @GetMapping("/newId")
    public Map<String,String> newId() { return Map.of("empId", es.generateNewEmpId()); }
    @GetMapping("/jobs")
    public List<?> jobs() { return js.findAll(); }
}