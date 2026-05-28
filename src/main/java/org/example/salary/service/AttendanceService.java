package org.example.salary.service;

import org.example.salary.entity.Attendance;
import org.example.salary.dao.AttendanceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AttendanceService {
    private final AttendanceDao attendanceDao;
    public AttendanceService(AttendanceDao attendanceDao) { this.attendanceDao = attendanceDao; }
    public List<Attendance> findAll() { return attendanceDao.findAllWithEmp(); }
    public Attendance findById(String attId) { return attendanceDao.findById(attId); }
    public List<Attendance> search(String empId, String department, String attMonth) { return attendanceDao.search(empId, department, attMonth); }
    public List<Attendance> findByMonth(String month) { return attendanceDao.findByMonth(month); }
    @Transactional
    public void save(Attendance att) {
        if (attendanceDao.findById(att.getAttId()) != null) attendanceDao.update(att);
        else attendanceDao.insert(att);
    }
    @Transactional
    public void delete(String attId) { attendanceDao.delete(attId); }
}
