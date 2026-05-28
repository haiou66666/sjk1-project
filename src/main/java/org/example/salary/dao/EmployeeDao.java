package org.example.salary.dao;

import org.example.salary.entity.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeDao {
    private final JdbcTemplate jdbc;

    public EmployeeDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<Employee> rowMapper = (rs, rn) -> {
        Employee e = new Employee();
        e.setEmpId(rs.getString("emp_id"));
        e.setEmpName(rs.getString("emp_name"));
        e.setDepartment(rs.getString("department"));
        e.setJobId(rs.getString("job_id"));
        e.setHireDate(rs.getDate("hire_date") != null ? rs.getDate("hire_date").toLocalDate() : null);
        e.setPhone(rs.getString("phone"));
        e.setStatus(rs.getString("status"));
        return e;
    };

    public List<Employee> findAll() {
        return jdbc.query("SELECT * FROM employee", rowMapper);
    }

    public Employee findById(String empId) {
        List<Employee> list = jdbc.query("SELECT * FROM employee WHERE emp_id = ?", rowMapper, empId);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Employee> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        String kw = "%" + keyword.trim() + "%";
        return jdbc.query("SELECT * FROM employee WHERE emp_name LIKE ? OR emp_id LIKE ?", rowMapper, kw, kw);
    }

    public int insert(Employee e) {
        return jdbc.update("INSERT INTO employee(emp_id,emp_name,department,job_id,hire_date,phone,status) VALUES(?,?,?,?,?,?,?)",
                e.getEmpId(), e.getEmpName(), e.getDepartment(), e.getJobId(), e.getHireDate(), e.getPhone(), e.getStatus());
    }

    public int update(Employee e) {
        return jdbc.update("UPDATE employee SET emp_name=?,department=?,job_id=?,hire_date=?,phone=?,status=? WHERE emp_id=?",
                e.getEmpName(), e.getDepartment(), e.getJobId(), e.getHireDate(), e.getPhone(), e.getStatus(), e.getEmpId());
    }

    public int delete(String empId) {
        return jdbc.update("DELETE FROM employee WHERE emp_id=?", empId);
    }

    public int countActiveByDept(String department) {
        Integer c = jdbc.queryForObject("SELECT COUNT(*) FROM employee WHERE department=? AND status='在职'", Integer.class, department);
        return c != null ? c : 0;
    }
}
