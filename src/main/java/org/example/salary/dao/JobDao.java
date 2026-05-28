package org.example.salary.dao;

import org.example.salary.entity.Job;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JobDao {
    private final JdbcTemplate jdbc;
    public JobDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<Job> rowMapper = (rs, rn) -> {
        Job j = new Job();
        j.setJobId(rs.getString("job_id"));
        j.setJobName(rs.getString("job_name"));
        j.setJobLevel(rs.getString("job_level"));
        j.setBaseSalary(rs.getBigDecimal("base_salary"));
        j.setRemark(rs.getString("remark"));
        return j;
    };

    public List<Job> findAll() { return jdbc.query("SELECT * FROM job", rowMapper); }

    public Job findById(String jobId) {
        List<Job> list = jdbc.query("SELECT * FROM job WHERE job_id = ?", rowMapper, jobId);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Job> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        String kw = "%" + keyword.trim() + "%";
        return jdbc.query("SELECT * FROM job WHERE job_name LIKE ? OR job_level LIKE ?", rowMapper, kw, kw);
    }

    public int insert(Job j) {
        return jdbc.update("INSERT INTO job(job_id,job_name,job_level,base_salary,remark) VALUES(?,?,?,?,?)",
                j.getJobId(), j.getJobName(), j.getJobLevel(), j.getBaseSalary(), j.getRemark());
    }

    public int update(Job j) {
        return jdbc.update("UPDATE job SET job_name=?,job_level=?,base_salary=?,remark=? WHERE job_id=?",
                j.getJobName(), j.getJobLevel(), j.getBaseSalary(), j.getRemark(), j.getJobId());
    }

    public int delete(String jobId) { return jdbc.update("DELETE FROM job WHERE job_id=?", jobId); }
}
