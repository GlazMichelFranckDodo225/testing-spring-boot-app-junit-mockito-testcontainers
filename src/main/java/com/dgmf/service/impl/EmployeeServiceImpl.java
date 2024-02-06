package com.dgmf.service.impl;

import com.dgmf.entity.Employee;
import com.dgmf.exception.ResourceNotFoundException;
import com.dgmf.repository.EmployeeRepository;
import com.dgmf.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    // @Autowired
    private final EmployeeRepository employeeRepository;

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

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository
                .findById(employeeId);

        return optionalEmployee;
    }

    @Override
    public Employee updateEmployee(Employee updateEmployee) {
        /*// Retrieve Employee from The DB
        Employee foundEmployee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new RuntimeException(
                                "Employee with Id " + employeeId +
                                        " NOT FOUND"
                        )
                );

        foundEmployee.setFirstName(employee.getFirstName());
        foundEmployee.setLastName(employee.getLastName());
        foundEmployee.setEmail(employee.getEmail());*/

        Employee updatedEmployee = employeeRepository.save(updateEmployee);

        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
