package org.example.salary.controller;

import org.example.salary.entity.Job;
import org.example.salary.service.JobService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/job")
public class JobController {
    private final JobService js;
    public JobController(JobService js) { this.js = js; }

    @GetMapping
    public List<Job> list(@RequestParam(required = false) String keyword) { return js.search(keyword); }
    @GetMapping("/{id}")
    public Job get(@PathVariable String id) { return js.findById(id); }
    @PostMapping
    public Map<String,Object> save(@RequestBody Job job) { js.save(job); return Map.of("success", true); }
    @DeleteMapping("/{id}")
    public Map<String,Object> delete(@PathVariable String id) { js.delete(id); return Map.of("success", true); }
    @GetMapping("/newId")
    public Map<String,String> newId() { return Map.of("jobId", js.generateNewJobId()); }
}