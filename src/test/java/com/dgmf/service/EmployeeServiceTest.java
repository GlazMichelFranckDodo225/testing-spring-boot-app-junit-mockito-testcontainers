package com.dgmf.service;

import com.dgmf.entity.Employee;
import com.dgmf.repository.EmployeeRepository;
import com.dgmf.service.impl.EmployeeServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

public class EmployeeServiceTest {
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    // This Method will be executed before each JUnit Test belongs
    // to the "EmployeeServiceTest" Class
    @BeforeEach
    public void setup() {
        // Mock "EmployeeRepository"
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        // Inject "EmployeeRepository" in "EmployeeService"
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    // JUnit Test for Save Employee Method
    @Test
    @DisplayName("JUnit Test for Save Employee Method")
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        /* Given - Precondition or Setup */
        // Create Employee Object
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();

        // To Stub (to Mock) "findEmployeeByEmail()" Method of "EmployeeRepository"
        BDDMockito
                .given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        // To Stub (to Mock) "save()" Method of "EmployeeRepository"
        BDDMockito
                .given(employeeRepository.save(employee))
                .willReturn(employee);

        // Custom Outputs
        System.out.println(employeeRepository);
        System.out.println(employeeService);

        /* When - Action or the Behavior that we are going to test */
        Employee saveEmployee = employeeService.saveEmployee(employee);

        // Custom Output
        System.out.println(saveEmployee);

        /* Then - Verify the Output */
        Assertions.assertThat(saveEmployee).isNotNull();
    }
}
