package org.example.salary.controller;

import org.example.salary.entity.Attendance;
import org.example.salary.service.AttendanceService;
import org.example.salary.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService as;
    private final EmployeeService es;
    public AttendanceController(AttendanceService as, EmployeeService es) { this.as = as; this.es = es; }

    @GetMapping
    public List<Attendance> list(@RequestParam(required = false) String empId,
                                  @RequestParam(required = false) String department,
                                  @RequestParam(required = false) String attMonth) {
        return as.search(empId, department, attMonth);
    }
    @GetMapping("/{id}")
    public Attendance get(@PathVariable String id) { return as.findById(id); }
    @PostMapping
    public Map<String,Object> save(@RequestBody Attendance att) { as.save(att); return Map.of("success", true); }
    @DeleteMapping("/{id}")
    public Map<String,Object> delete(@PathVariable String id) { as.delete(id); return Map.of("success", true); }
    @GetMapping("/employees")
    public List<?> employees() { return es.findAll(); }
}