package org.example.salary.service;

import org.example.salary.entity.Employee;
import org.example.salary.dao.EmployeeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeDao employeeDao;
    public EmployeeService(EmployeeDao employeeDao) { this.employeeDao = employeeDao; }
    public List<Employee> findAll() { return employeeDao.findAll(); }
    public Employee findById(String empId) { return employeeDao.findById(empId); }
    public List<Employee> search(String keyword) { return employeeDao.search(keyword); }
    @Transactional
    public void save(Employee emp) {
        if (employeeDao.findById(emp.getEmpId()) != null) employeeDao.update(emp);
        else employeeDao.insert(emp);
    }
    @Transactional
    public void delete(String empId) { employeeDao.delete(empId); }
    public String generateNewEmpId() {
        List<Employee> all = employeeDao.findAll();
        int maxNum = 0;
        for (Employee e : all) {
            try { int n = Integer.parseInt(e.getEmpId().replace("EMP","")); if (n > maxNum) maxNum = n; } catch (Exception ignored) {}
        }
        return String.format("EMP%03d", maxNum + 1);
    }
}
