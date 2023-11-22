package com.dgmf.service.impl;

import com.dgmf.entity.Employee;
import com.dgmf.exception.ResourceNotFoundException;
import com.dgmf.repository.EmployeeRepository;
import com.dgmf.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        // Find Requested Employee By Email
        Optional<Employee> existingEmployee = employeeRepository
                .findEmployeeByEmail(employee.getEmail());

        // Check if the Requested Employee already exist
        if(existingEmployee.isPresent()) {
            throw new
                    ResourceNotFoundException("Employee already exist " +
                    "with given email : " + employee.getEmail());
        }

        // Save Requested Employee
        Employee savedEmployee = employeeRepository.save(employee);

        return savedEmployee;
    }
}
