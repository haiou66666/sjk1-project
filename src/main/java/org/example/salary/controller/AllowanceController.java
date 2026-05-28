package org.example.salary.controller;

import org.example.salary.entity.Allowance;
import org.example.salary.service.AllowanceService;
import org.example.salary.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/allowance")
public class AllowanceController {
    private final AllowanceService as;
    private final EmployeeService es;
    public AllowanceController(AllowanceService as, EmployeeService es) { this.as = as; this.es = es; }

    @GetMapping
    public List<Allowance> list(@RequestParam(required = false) String empId,
                                 @RequestParam(required = false) String overtimeType,
                                 @RequestParam(required = false) String attMonth) {
        return as.search(empId, overtimeType, attMonth);
    }
    @PostMapping
    public Map<String,Object> save(@RequestBody Allowance allow) { as.save(allow); return Map.of("success", true); }
    @DeleteMapping("/{id}")
    public Map<String,Object> delete(@PathVariable String id) { as.delete(id); return Map.of("success", true); }
    @GetMapping("/employees")
    public List<?> employees() { return es.findAll(); }
}