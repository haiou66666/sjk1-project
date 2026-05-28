package org.example.salary.dao;

import org.example.salary.entity.MonthlySalary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MonthlySalaryDao {
    private final JdbcTemplate jdbc;
    public MonthlySalaryDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<MonthlySalary> rowMapper = (rs, rn) -> {
        MonthlySalary ms = new MonthlySalary();
        ms.setSalaryId(rs.getString("salary_id"));
        ms.setEmpId(rs.getString("emp_id"));
        ms.setSalaryMonth(rs.getString("salary_month"));
        ms.setBaseSalary(rs.getBigDecimal("base_salary"));
        ms.setAllowanceTotal(rs.getBigDecimal("allowance_total"));
        ms.setDeduction(rs.getBigDecimal("deduction"));
        ms.setNetSalary(rs.getBigDecimal("net_salary"));
        ms.setCalcTime(rs.getTimestamp("calc_time") != null ? rs.getTimestamp("calc_time").toLocalDateTime() : null);
        ms.setOperator(rs.getString("operator"));
        try { ms.setEmpName(rs.getString("emp_name")); } catch (Exception ignored) {}
        try { ms.setDepartment(rs.getString("department")); } catch (Exception ignored) {}
        try { ms.setJobName(rs.getString("job_name")); } catch (Exception ignored) {}
        return ms;
    };

    public List<MonthlySalary> findAllWithEmp() {
        return jdbc.query("SELECT ms.*, e.emp_name, e.department, j.job_name FROM monthly_salary ms JOIN employee e ON ms.emp_id=e.emp_id JOIN job j ON e.job_id=j.job_id ORDER BY ms.salary_month DESC", rowMapper);
    }

    public List<MonthlySalary> search(String empId, String empName, String salaryMonth, Integer year) {
        StringBuilder sql = new StringBuilder("SELECT ms.*, e.emp_name, e.department, j.job_name FROM monthly_salary ms JOIN employee e ON ms.emp_id=e.emp_id JOIN job j ON e.job_id=j.job_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (empId != null && !empId.isEmpty()) { sql.append(" AND ms.emp_id=?"); params.add(empId); }
        if (empName != null && !empName.isEmpty()) { sql.append(" AND e.emp_name LIKE ?"); params.add("%" + empName + "%"); }
        if (salaryMonth != null && !salaryMonth.isEmpty()) { sql.append(" AND ms.salary_month=?"); params.add(salaryMonth); }
        if (year != null) { sql.append(" AND ms.salary_month LIKE ?"); params.add(year + "-%"); }
        sql.append(" ORDER BY ms.salary_month DESC");
        return jdbc.query(sql.toString(), rowMapper, params.toArray());
    }

    public List<MonthlySalary> findByDeptMonth(String department, String salaryMonth) {
        StringBuilder sql = new StringBuilder("SELECT ms.*, e.emp_name, e.department, j.job_name FROM monthly_salary ms JOIN employee e ON ms.emp_id=e.emp_id JOIN job j ON e.job_id=j.job_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (department != null && !department.isEmpty()) { sql.append(" AND e.department=?"); params.add(department); }
        if (salaryMonth != null && !salaryMonth.isEmpty()) { sql.append(" AND ms.salary_month=?"); params.add(salaryMonth); }
        sql.append(" ORDER BY e.emp_id");
        return jdbc.query(sql.toString(), rowMapper, params.toArray());
    }

    public BigDecimal sumYearSalary(String empId, int year) {
        BigDecimal r = jdbc.queryForObject("SELECT COALESCE(SUM(net_salary),0) FROM monthly_salary WHERE emp_id=? AND salary_month LIKE ?", BigDecimal.class, empId, year + "-%");
        return r != null ? r : BigDecimal.ZERO;
    }

    public int insert(MonthlySalary ms) {
        return jdbc.update("INSERT INTO monthly_salary(salary_id,emp_id,salary_month,base_salary,allowance_total,deduction,net_salary,calc_time,operator) VALUES(?,?,?,?,?,?,?,NOW(),?)",
                ms.getSalaryId(), ms.getEmpId(), ms.getSalaryMonth(), ms.getBaseSalary(), ms.getAllowanceTotal(), ms.getDeduction(), ms.getNetSalary(), ms.getOperator());
    }

    public int update(MonthlySalary ms) {
        return jdbc.update("UPDATE monthly_salary SET base_salary=?,allowance_total=?,deduction=?,net_salary=?,calc_time=NOW(),operator=? WHERE salary_id=?",
                ms.getBaseSalary(), ms.getAllowanceTotal(), ms.getDeduction(), ms.getNetSalary(), ms.getOperator(), ms.getSalaryId());
    }

    public int exists(String empId, String month) {
        Integer r = jdbc.queryForObject("SELECT COUNT(*) FROM monthly_salary WHERE emp_id=? AND salary_month=?", Integer.class, empId, month);
        return r != null ? r : 0;
    }
}
