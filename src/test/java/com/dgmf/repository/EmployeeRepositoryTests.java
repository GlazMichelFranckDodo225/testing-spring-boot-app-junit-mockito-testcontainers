package com.dgmf.repository;

import com.dgmf.entity.Employee;
// import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.h2.mvstore.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Nolibée")
                .lastName("Perceval")
                .email("nolibeeperceval@gmail.com")
                .build();
    }

    // JUnit Test for Save Employee operation
    @Test
    @DisplayName("JUnit Test for Save Employee operation")
    void givenEmployeeObject_whenSave_thenReturnsSavedEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();*/

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // Then - Verify the Output
        /*Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);*/
        // import static org.assertj.core.api.Assertions.assertThat
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit Test for Get All Employees operation
    @Test
    @DisplayName("JUnit Test for Get All Employees operation")
    void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // Given - Precondition or Setup
        /*Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();*/

        Employee employee2 = Employee.builder()
                .firstName("Jean")
                .lastName("Dupont")
                .email("jeandupont@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // When - Action or the Behavior that we are going to test
        List<Employee> employeesList = employeeRepository.findAll();

        // Then - Verify the Output
        assertThat(employeesList).isNotNull();
        assertThat(employeesList.size()).isEqualTo(2);
    }

    // JUnit Test for Get Employee By Id Operation
    @Test
    @DisplayName("JUnit Test for Get Employee By Id operation")
    void givenEmployee_whenFindById_thenReturnsEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("Méliane")
                .lastName("Dodo")
                .email("melianedodo.com")
                .build();*/

        employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        Employee employeeDB = employeeRepository
                .findById(employee.getId()).get();

        // Then - Verify the Output
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getEmail())
                .isEqualTo("nolibeeperceval@gmail.com");
    }

    // JUnit Test for Get Employee By Email Operation
    @Test
    @DisplayName("JUnit Test for Get Employee By Email Operation")
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        Employee employeeDB = employeeRepository
                .findEmployeeByEmail(employee.getEmail()).get();

        // Then - Verify the Output
        assertThat(employeeDB).isNotNull();
    }

    // JUnit Test for Update Employee Operation
    @Test
    @DisplayName("JUnit Test for Update Employee Operation")
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmpoyee() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository
                .findById(employee.getId()).get();

        savedEmployee.setEmail("johndervish@gmail.com");
        savedEmployee.setFirstName("John Dervish");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // Then - Verify the Output
        assertThat(updatedEmployee.getEmail())
                .isEqualTo("johndervish@gmail.com");
        assertThat(updatedEmployee.getFirstName())
                .isEqualTo("John Dervish");
    }

    // JUnit Test for Delete Employee By Id Operation
    @Test
    @DisplayName("JUnit Test for Delete Employee Operation")
    void givenEmployeeObject_whenDeleteEmployeeById_thenRemoveEmployee() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        // Then - Verify the Output
        assertThat(optionalEmployee).isEmpty();
    }

    // JUnit Test for Custom Query using JPQL with Index Params
    @Test
    @DisplayName("JUnit Test for Custom Query using JPQL with Index Params")
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        /*String firstName = "John";
        String lastName = "Doe";*/

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(
                employee.getFirstName(),
                employee.getLastName()
        );

        // Then - Verify the Output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Nolibée");
        assertThat(savedEmployee.getLastName()).isEqualTo("Perceval");
    }

    // JUnit Test for Custom Query using JPQL with Named Params
    @Test
    @DisplayName("JUnit Test for Custom Query using JPQL with Named Params")
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        /*String firstName = "John";
        String lastName = "Doe";*/

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository
                .findByJPQLNamedParams(
                        employee.getFirstName(),
                        employee.getLastName()
                );

        // Then - Verify the Output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Nolibée");
        assertThat(savedEmployee.getLastName()).isEqualTo("Perceval");
    }

    // JUnit Test for Custom Query using Native JPQL with Index Params
    @Test
    @DisplayName("JUnit Test for Custom Query using Native JPQL with Index Params")
    void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository
                .findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // Then - Verify the Output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Nolibée");
        assertThat(savedEmployee.getLastName()).isEqualTo("Perceval");
    }

    // JUnit Test for Custom Query using Native JPQL with Named Params
    @Test
    @DisplayName("JUnit Test for Custom Query using Native JPQL with Named Params")
    void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        // Given - Precondition or Setup
        /*Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.com")
                .build();*/

        employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository
                .findByNativeSQLNamedParams(
                        employee.getFirstName(),
                        employee.getLastName()
                );

        // Then - Verify the Output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Nolibée");
        assertThat(savedEmployee.getLastName()).isEqualTo("Perceval");
    }
}
