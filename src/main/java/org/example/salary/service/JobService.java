package org.example.salary.service;

import org.example.salary.entity.Job;
import org.example.salary.dao.JobDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class JobService {
    private final JobDao jobDao;
    public JobService(JobDao jobDao) { this.jobDao = jobDao; }
    public List<Job> findAll() { return jobDao.findAll(); }
    public Job findById(String jobId) { return jobDao.findById(jobId); }
    public List<Job> search(String keyword) { return jobDao.search(keyword); }
    @Transactional
    public void save(Job job) {
        if (jobDao.findById(job.getJobId()) != null) jobDao.update(job);
        else jobDao.insert(job);
    }
    @Transactional
    public void delete(String jobId) { jobDao.delete(jobId); }
    public String generateNewJobId() {
        List<Job> all = jobDao.findAll();
        int maxNum = 0;
        for (Job j : all) {
            try { int n = Integer.parseInt(j.getJobId().replace("J","")); if (n > maxNum) maxNum = n; } catch (Exception ignored) {}
        }
        return String.format("J%03d", maxNum + 1);
    }
}
