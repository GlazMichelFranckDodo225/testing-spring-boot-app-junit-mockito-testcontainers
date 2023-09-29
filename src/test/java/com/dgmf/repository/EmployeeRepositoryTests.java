package com.dgmf.repository;

import com.dgmf.entity.Employee;
// import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.h2.mvstore.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    // JUnit Test for Save Employee operation
    @Test
    @DisplayName("JUnit Test for Save Employee operation")
    void givenEmployeeObject_whenSave_thenReturnsSavedEmployeeObject() {
        // Given - Precondition or Setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();

        // When - Action or the Behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // Then - Verify the Output
        /*Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);*/
        // import static org.assertj.core.api.Assertions.assertThat
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }
}
