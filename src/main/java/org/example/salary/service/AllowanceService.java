package org.example.salary.service;

import org.example.salary.entity.Allowance;
import org.example.salary.dao.AllowanceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AllowanceService {
    private final AllowanceDao allowanceDao;
    public AllowanceService(AllowanceDao allowanceDao) { this.allowanceDao = allowanceDao; }
    public List<Allowance> findAll() { return allowanceDao.findAllWithEmp(); }
    public List<Allowance> search(String empId, String overtimeType, String attMonth) { return allowanceDao.search(empId, overtimeType, attMonth); }
    public BigDecimal sumByEmpMonth(String empId, String month) { return allowanceDao.sumByEmpMonth(empId, month); }
    @Transactional
    public void save(Allowance allow) { allowanceDao.insert(allow); }
    @Transactional
    public void update(Allowance allow) { allowanceDao.update(allow); }
    @Transactional
    public void delete(String allowId) { allowanceDao.delete(allowId); }
}
