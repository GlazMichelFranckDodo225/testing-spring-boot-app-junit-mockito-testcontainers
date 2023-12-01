package com.dgmf.service;

import com.dgmf.entity.Employee;
import com.dgmf.exception.ResourceNotFoundException;
import com.dgmf.repository.EmployeeRepository;
import com.dgmf.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock // Mock "EmployeeRepository"
    private EmployeeRepository employeeRepository;
    @InjectMocks // Inject "EmployeeRepository" in "EmployeeService"
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    // This Method will be executed before each JUnit Test belongs
    // to the "EmployeeServiceTest" Class
    @BeforeEach
    public void setup() {
        // Create Employee Object
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();
    }

    // JUnit Test for Save Employee Method
    @Test
    @DisplayName("JUnit Test for Save Employee Method")
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        /* Given - Precondition or Setup */
        // Condition met by the "setup()" method above

        // To Stub (to Mock) "findEmployeeByEmail()" Method
        // of "EmployeeRepository" (Static Import)
        given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        // To Stub (to Mock) "save()" Method of "EmployeeRepository" (Static Import)
        given(employeeRepository.save(employee))
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

    // JUnit Test for Save Employee Method which throws Exception
    @Test
    @DisplayName("JUnit Test for Save Employee Method which throws Exception")
    void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        /* Given - Precondition or Setup */
        // Condition met by the "setup()" method above

        // To Stub (to Mock) "findEmployeeByEmail()" Method
        // of "EmployeeRepository" (Static Import)
        given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee)); // Return Employee Object

        // Because of Exception which stop the Process, this Stubbing is unnecessary
        // To Stub (to Mock) "save()" Method of "EmployeeRepository" (Static Import)
        /*given(employeeRepository.save(employee))
                .willReturn(employee);*/

        // Custom Outputs
        System.out.println(employeeRepository);
        System.out.println(employeeService);

        /* When - Action or the Behavior that we are going to test */
        org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.saveEmployee(employee)
            );

        /* Then - Verify the Output */
        // Verifying that, after throwing "ResourceNotFoundException",
        // "employeeRepository.save(employee)" was never called into
        // "saveEmployee()" Method of the "EmployeeServiceImpl" Class
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
