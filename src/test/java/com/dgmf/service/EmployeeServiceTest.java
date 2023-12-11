package com.dgmf.service;

import com.dgmf.entity.Employee;
import com.dgmf.exception.ResourceNotFoundException;
import com.dgmf.repository.EmployeeRepository;
import com.dgmf.service.impl.EmployeeServiceImpl;
// import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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
        // Assertions.assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee).isNotNull();
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

    // JUnit Test for Get All Employees Method - Positive Scenario
    @Test
    @DisplayName("JUnit Test for Get All Employees Method - Positive Scenario")
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // Given - Precondition or Setup
        // Create Employee Object
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Mark")
                .lastName("Irish")
                .email("markirish@gmail.com")
                .build();

        given(employeeRepository.findAll())
                .willReturn(List.of(employee, employee2));

        // When - Action or the Behavior that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Then - Verify the Output
        /*Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);*/
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit Test for Get All Employees Method - Negative Scenario
    @Test
    @DisplayName("JUnit Test for Get All Employees Method - Negative Scenario")
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // Given - Precondition or Setup
        // Create Employee Object
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Mark")
                .lastName("Irish")
                .email("markirish@gmail.com")
                .build();

        given(employeeRepository.findAll())
                .willReturn(Collections.emptyList());

        // When - Action or the Behavior that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Then - Verify the Output
        /*Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);*/
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit Test for Get Employee By Id Method
    @Test
    @DisplayName("JUnit Test for Get Employee By Id Method")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // Given - Precondition or Setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee =
                employeeService.getEmployeeById(employee.getId()).get();

        // Then - Verify the Output
        // Assertions.assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit Test for Update Employee Method
    @Test
    @DisplayName("JUnit Test for Update Employee Method")
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Given - Precondition or Setup
        // To Stub JpaRepository Repository "save()" Method and Configure the
        // Response for this Method
        given(employeeRepository.save(employee)).willReturn(employee);
        // Update Employee
        employee.setFirstName("Jonatan");
        employee.setEmail("jonatandoe@gmail.com");

        // When - Action or the Behavior that we are going to test
        Employee updatedemployee = employeeService.updateEmployee(employee);

        // Then - Verify the Output
        assertThat(updatedemployee.getFirstName())
                .isEqualTo("Jonatan");
        assertThat(updatedemployee.getEmail())
                .isEqualTo("jonatandoe@gmail.com");
    }

    // JUnit Test for Delete Employee By Id Method
    // Remind that the Return Type of "deleteById()" JpaRepository
    // Method is "void" (Nothing)
    @Test
    @DisplayName("JUnit Test for Delete Employee By Id Method")
    void givenEmployeeId_whenDeleteEmployeeById_thenReturnNothing() {
        // Given - Precondition or Setup
        Long employeeId = 1L;

        // To Stub JpaRepository Repository "deleteById()" Method and Configure
        // the Response for this Method
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // When - Action or the Behavior that we are going to test
        employeeService.deleteEmployeeById(employeeId);

        // Then - Verify the Output
        // Checks that the Method has been called at least 1 time
        verify(employeeRepository, times(1))
                .deleteById(employeeId);
    }
}
