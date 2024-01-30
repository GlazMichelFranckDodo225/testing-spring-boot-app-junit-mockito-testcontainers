package com.dgmf.controller;

import com.dgmf.entity.Employee;
import com.dgmf.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RequestMapping(value = "/api/v1/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    /*@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }*/
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee
    ) {
        return new ResponseEntity<>(
                employeeService.saveEmployee(employee),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable("id") Long employeeId
    ) {
        // Notice: "getEmployeeById" return Optional Employee from
        // Service Layer (and from DB)
        return employeeService.getEmployeeById(employeeId)
                // To Configure Response Status Code using
                // "map()" of Optional Class
                .map(ResponseEntity::ok)
                // In Case of "null" or "empty"
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 1 ==> Retrieve Employee from DB with
    // "employeeService.getEmployeeById()" Method
    // 2 ==> Update Retrieved Employee from DB with
    // "employeeService.updateEmployee()" Method
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable("id") Long employeeId,
            @RequestBody Employee employee
    ) {
        // Notice: "getEmployeeById" return Optional Employee from
        // Service Layer (and from DB)
        return employeeService.getEmployeeById(employeeId)
                // To Update "employeeFromDb" with "employee" using
                // "map()" of Optional Class
                .map(employeeFromDb -> {
                    employeeFromDb.setFirstName(employee.getFirstName());
                    employeeFromDb.setLastName(employee.getLastName());
                    employeeFromDb.setEmail(employee.getEmail());

                    Employee updatedEmployee = employeeService
                            .updateEmployee(employeeFromDb);

                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                // To Configure Response Status Code in Case of "null" or "empty"
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
