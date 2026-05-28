package org.example.salary.dao;

import org.example.salary.entity.Allowance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AllowanceDao {
    private final JdbcTemplate jdbc;
    public AllowanceDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<Allowance> rowMapper = (rs, rn) -> {
        Allowance a = new Allowance();
        a.setAllowId(rs.getString("allow_id"));
        a.setEmpId(rs.getString("emp_id"));
        a.setOvertimeType(rs.getString("overtime_type"));
        a.setOvertimeAmount(rs.getBigDecimal("overtime_amount"));
        a.setAllowanceRate(rs.getBigDecimal("allowance_rate"));
        a.setAllowanceAmount(rs.getBigDecimal("allowance_amount"));
        a.setOvertimeDate(rs.getDate("overtime_date") != null ? rs.getDate("overtime_date").toLocalDate() : null);
        a.setAttMonth(rs.getString("att_month"));
        try { a.setEmpName(rs.getString("emp_name")); } catch (Exception ignored) {}
        try { a.setDepartment(rs.getString("department")); } catch (Exception ignored) {}
        return a;
    };

    public List<Allowance> findAllWithEmp() {
        return jdbc.query("SELECT a.*, e.emp_name, e.department FROM allowance a JOIN employee e ON a.emp_id=e.emp_id ORDER BY a.overtime_date DESC", rowMapper);
    }

    public List<Allowance> search(String empId, String overtimeType, String attMonth) {
        StringBuilder sql = new StringBuilder("SELECT a.*, e.emp_name, e.department FROM allowance a JOIN employee e ON a.emp_id=e.emp_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (empId != null && !empId.isEmpty()) { sql.append(" AND a.emp_id=?"); params.add(empId); }
        if (overtimeType != null && !overtimeType.isEmpty()) { sql.append(" AND a.overtime_type=?"); params.add(overtimeType); }
        if (attMonth != null && !attMonth.isEmpty()) { sql.append(" AND a.att_month=?"); params.add(attMonth); }
        sql.append(" ORDER BY a.overtime_date DESC");
        return jdbc.query(sql.toString(), rowMapper, params.toArray());
    }

    public BigDecimal sumByEmpMonth(String empId, String month) {
        BigDecimal r = jdbc.queryForObject("SELECT COALESCE(SUM(allowance_amount),0) FROM allowance WHERE emp_id=? AND att_month=?", BigDecimal.class, empId, month);
        return r != null ? r : BigDecimal.ZERO;
    }

    public BigDecimal sumByEmpYear(String empId, int year) {
        BigDecimal r = jdbc.queryForObject("SELECT COALESCE(SUM(allowance_amount),0) FROM allowance WHERE emp_id=? AND YEAR(overtime_date)=?", BigDecimal.class, empId, year);
        return r != null ? r : BigDecimal.ZERO;
    }

    public int insert(Allowance a) {
        return jdbc.update("INSERT INTO allowance(allow_id,emp_id,overtime_type,overtime_amount,allowance_rate,allowance_amount,overtime_date,att_month) VALUES(?,?,?,?,?,?,?,?)",
                a.getAllowId(), a.getEmpId(), a.getOvertimeType(), a.getOvertimeAmount(), a.getAllowanceRate(), a.getAllowanceAmount(), a.getOvertimeDate(), a.getAttMonth());
    }

    public int update(Allowance a) {
        return jdbc.update("UPDATE allowance SET overtime_type=?,overtime_amount=?,allowance_rate=?,allowance_amount=?,overtime_date=? WHERE allow_id=?",
                a.getOvertimeType(), a.getOvertimeAmount(), a.getAllowanceRate(), a.getAllowanceAmount(), a.getOvertimeDate(), a.getAllowId());
    }

    public int delete(String allowId) { return jdbc.update("DELETE FROM allowance WHERE allow_id=?", allowId); }
}
