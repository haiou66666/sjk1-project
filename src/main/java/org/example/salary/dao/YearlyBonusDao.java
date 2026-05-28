package org.example.salary.dao;

import org.example.salary.entity.YearlyBonus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class YearlyBonusDao {
    private final JdbcTemplate jdbc;
    public YearlyBonusDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<YearlyBonus> rowMapper = (rs, rn) -> {
        YearlyBonus yb = new YearlyBonus();
        yb.setBonusId(rs.getString("bonus_id"));
        yb.setEmpId(rs.getString("emp_id"));
        yb.setYear(rs.getInt("year"));
        yb.setTotalSalary(rs.getBigDecimal("total_salary"));
        yb.setTotalAllowance(rs.getBigDecimal("total_allowance"));
        yb.setBonusAmount(rs.getBigDecimal("bonus_amount"));
        yb.setCalcTime(rs.getTimestamp("calc_time") != null ? rs.getTimestamp("calc_time").toLocalDateTime() : null);
        yb.setOperator(rs.getString("operator"));
        try { yb.setEmpName(rs.getString("emp_name")); } catch (Exception ignored) {}
        try { yb.setDepartment(rs.getString("department")); } catch (Exception ignored) {}
        return yb;
    };

    public List<YearlyBonus> findAllWithEmp() {
        return jdbc.query("SELECT yb.*, e.emp_name, e.department FROM yearly_bonus yb JOIN employee e ON yb.emp_id=e.emp_id ORDER BY yb.year DESC", rowMapper);
    }

    public List<YearlyBonus> search(String empId, String department, Integer year) {
        StringBuilder sql = new StringBuilder("SELECT yb.*, e.emp_name, e.department FROM yearly_bonus yb JOIN employee e ON yb.emp_id=e.emp_id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (empId != null && !empId.isEmpty()) { sql.append(" AND yb.emp_id=?"); params.add(empId); }
        if (department != null && !department.isEmpty()) { sql.append(" AND e.department=?"); params.add(department); }
        if (year != null) { sql.append(" AND yb.year=?"); params.add(year); }
        sql.append(" ORDER BY yb.year DESC");
        return jdbc.query(sql.toString(), rowMapper, params.toArray());
    }

    public int insertOrUpdate(YearlyBonus yb) {
        return jdbc.update("INSERT INTO yearly_bonus(bonus_id,emp_id,year,total_salary,total_allowance,bonus_amount,calc_time,operator) VALUES(?,?,?,?,?,?,NOW(),?) ON DUPLICATE KEY UPDATE total_salary=VALUES(total_salary),total_allowance=VALUES(total_allowance),bonus_amount=VALUES(bonus_amount),calc_time=NOW()",
                yb.getBonusId(), yb.getEmpId(), yb.getYear(), yb.getTotalSalary(), yb.getTotalAllowance(), yb.getBonusAmount(), yb.getOperator());
    }

    public int delete(String bonusId) { return jdbc.update("DELETE FROM yearly_bonus WHERE bonus_id=?", bonusId); }
}
