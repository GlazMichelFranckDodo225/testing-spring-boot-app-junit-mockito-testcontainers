package com.dgmf.service;

import com.dgmf.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long employeeId);
    Employee updateEmployee(Employee updateEmployee);
    void deleteEmployeeById(Long employeeId);
}
