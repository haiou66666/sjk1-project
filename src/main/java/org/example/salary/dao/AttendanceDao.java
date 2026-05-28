package org.example.salary.dao;

import org.example.salary.entity.Attendance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceDao {
    private final JdbcTemplate jdbc;
    public AttendanceDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<Attendance> rowMapper = (rs, rn) -> {
        Attendance a = new Attendance();
        a.setAttId(rs.getString("att_id"));
        a.setEmpId(rs.getString("emp_id"));
        a.setAttMonth(rs.getString("att_month"));
        a.setWorkDays(rs.getInt("work_days"));
        a.setLeaveDays(rs.getBigDecimal("leave_days"));
        a.setOvertimeHours(rs.getBigDecimal("overtime_hours"));
        a.setOvertimeDays(rs.getBigDecimal("overtime_days"));
        a.setAttRemark(rs.getString("att_remark"));
        try { a.setEmpName(rs.getString("emp_name")); } catch (Exception ignored) {}
        try { a.setDepartment(rs.getString("department")); } catch (Exception ignored) {}
        return a;
    };

    public List<Attendance> findAllWithEmp() {
        return jdbc.query("SELECT a.*, e.emp_name, e.department FROM attendance a JOIN employee e ON a.emp_id=e.emp_id ORDER BY a.att_month DESC", rowMapper);
    }

    public Attendance findById(String attId) {
        List<Attendance> list = jdbc.query("SELECT a.*, e.emp_name, e.department FROM attendance a JOIN employee e ON a.emp_id=e.emp_id WHERE a.att_id=?", rowMapper, attId);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Attendance> search(String empId, String department, String attMonth) {
        StringBuilder sql = new StringBuilder("SELECT a.*, e.emp_name, e.department FROM attendance a JOIN employee e ON a.emp_id=e.emp_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (empId != null && !empId.isEmpty()) { sql.append(" AND a.emp_id=?"); params.add(empId); }
        if (department != null && !department.isEmpty()) { sql.append(" AND e.department=?"); params.add(department); }
        if (attMonth != null && !attMonth.isEmpty()) { sql.append(" AND a.att_month=?"); params.add(attMonth); }
        sql.append(" ORDER BY a.att_month DESC");
        return jdbc.query(sql.toString(), rowMapper, params.toArray());
    }

    public List<Attendance> findByMonth(String month) {
        return jdbc.query("SELECT a.*, e.emp_name, e.department FROM attendance a JOIN employee e ON a.emp_id=e.emp_id WHERE a.att_month=? ORDER BY e.department", rowMapper, month);
    }

    public int insert(Attendance a) {
        return jdbc.update("INSERT INTO attendance(att_id,emp_id,att_month,work_days,leave_days,overtime_hours,overtime_days,att_remark) VALUES(?,?,?,?,?,?,?,?)",
                a.getAttId(), a.getEmpId(), a.getAttMonth(), a.getWorkDays(), a.getLeaveDays(), a.getOvertimeHours(), a.getOvertimeDays(), a.getAttRemark());
    }

    public int update(Attendance a) {
        return jdbc.update("UPDATE attendance SET work_days=?,leave_days=?,overtime_hours=?,overtime_days=?,att_remark=? WHERE att_id=?",
                a.getWorkDays(), a.getLeaveDays(), a.getOvertimeHours(), a.getOvertimeDays(), a.getAttRemark(), a.getAttId());
    }

    public int delete(String attId) { return jdbc.update("DELETE FROM attendance WHERE att_id=?", attId); }
}
