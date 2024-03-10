package com.dgmf.integration;

import com.dgmf.entity.Employee;
import com.dgmf.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// To Integrate TestContainers in JUnit Test Cases ==> Provide
// Integration of JUnit 5 with TestContainers
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // To Call REST APIs
public class EmployeeControllerIntegrationTestContainers {
    // This Test Container Will Be Shared Between Test Methods
    // @Testcontainers Annotation Will Manage the Life Cycle of
    // this Container
    @Container
    private static final MySQLContainer MySQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    // To Configure Testcontainers Database Properties
                    .withDatabaseName("ems")
                    .withUsername("username")
                    .withPassword("12345");
    // Injecting MockMvc Class to Make HTTP Requests using
    // "perform()" Method
    @Autowired
    private MockMvc mockMvc;
    // Injecting EmployeeRepository to Use its Methods to
    // Perform Different Operations on DB
    @Autowired
    private EmployeeRepository employeeRepository;
    // To Serialize and Deserialize Objects (Jackson Library)
    @Autowired
    private ObjectMapper objectMapper;

    // Will Be Executed Before Each JUnit Test
    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    // Start of Tests
    // Integration Test for Create Employee REST API
    @Test
    @DisplayName("Integration Test for Create Employee REST API")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee()
            throws Exception {
        // Testcontainers Outputs
        System.out.println("========= Testcontainers Outputs - Start =========");
        System.out.println("MySQL Testcontainers Database Name : " +
                MySQL_CONTAINER.getDatabaseName());
        System.out.println("MySQL Testcontainers Database Url : " +
                MySQL_CONTAINER.getJdbcUrl());
        System.out.println("MySQL Testcontainers Username : " +
                MySQL_CONTAINER.getUsername());
        System.out.println("MySQL Testcontainers Password : " +
                MySQL_CONTAINER.getPassword());
        System.out.println("========= Testcontainers Outputs - End =========");

        // Given - Precondition or Setup
        Employee employee = Employee.builder()
                .firstName("Xavi")
                .lastName("Disturb")
                .email("xavidisturb@gmail.com")
                .build();

        // When - Action or the Behavior that we are going to test
        // (2 Static Imports)
        ResultActions response = mockMvc.perform(
                // Make a POST REST API Call
                post("/api/v1/employees")
                        // Define the Content as JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        // To Convert Employee Object into JSON
                        .content(objectMapper.writeValueAsString(employee))
        );

        // Then - Verify the Output
        // Verify HTTP Status "201 CREATED" in the Response
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "201 CREATED" in the Response
                .andExpect(status().isCreated())
                // Using JSON Path Method to Check Actual Value ("$.firstName" and so on)
                // with the Expected Value ("employee.getFirstName()" and so on)
                .andExpect(jsonPath(
                                "$.firstName",
                                is(employee.getFirstName())
                        )
                )
                .andExpect(jsonPath(
                                "$.lastName",
                                is(employee.getLastName())
                        )
                )
                .andExpect(jsonPath(
                                "$.email",
                                is(employee.getEmail())
                        )
                );
    }

    // Integration Test for Get All Employees REST API
    @Test
    @DisplayName("Integration Test for Get All Employees REST API")
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList()
            throws Exception {
        // Given - Precondition or Setup
        List<Employee> employees = new ArrayList<>();
        employees.add(
                Employee.builder()
                        .firstName("Milhan")
                        .lastName("Norton")
                        .email("milhannorton@gmail.com")
                        .build()
        );

        employees.add(
                Employee.builder()
                        .firstName("Jeremy")
                        .lastName("O'hara")
                        .email("jeremyohara@gmail.com")
                        .build()
        );

        List<Employee> savedEmployees = employeeRepository.saveAll(employees);

        // When - Action or the Behavior that we are going to test
        // We Have Made a Get Employees REST API Call and Retrieve the Response
        ResultActions response = mockMvc.perform(get("/api/v1/employees"));

        // Then - Verify the Output
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "200 OK" in the Response
                .andExpect(status().isOk())
                // Using JSON Path Method to Check the Size of the
                // Returned JSON Array
                .andExpect(jsonPath(
                                "$.size()",
                                is(employees.size())
                        )
                );
    }

    // Integration Test for Get Employee By Id REST API
    //  Positive Scenario with Valid Employee Id
    @Test
    @DisplayName("Integration Test for Get Employee By Id REST API")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // Given - Precondition or Setup
        // Given Id
        // Long employeeId = 1L;
        // Employee to Recover
        Employee employee = Employee.builder()
                .firstName("Manuel")
                .lastName("Ortega")
                .email("manuelortega@gmail.com")
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        ResultActions response = mockMvc.perform(get(
                        "/api/v1/employees/{id}",
                        //employeeId
                        savedEmployee.getId()
                )
        );

        // Then - Verify the Output
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "200 OK" in the Response
                .andExpect(status().isOk())
                // Using JSON Path Method to Verify the Actual
                // Value with the Expected Value
                .andExpect(jsonPath(
                                "$.firstName",
                                is(employee.getFirstName())
                        )
                )
                .andExpect(jsonPath(
                                "$.lastName",
                                is(employee.getLastName())
                        )
                )
                .andExpect(jsonPath(
                                "$.email",
                                is(employee.getEmail())
                        )
                );
    }

    // Integration Test for Get Employee By Id REST API
    //  Negative Scenario with No Valid Employee Id
    @Test
    @DisplayName("Integration Test for Get Employee By Id REST API")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty()
            throws Exception {
        // Given - Precondition or Setup
        // Given Id
        Long employeeId = 1L; // This Id Doesn't Exist into DB
        // Employee to Recover
        Employee employee = Employee.builder()
                .firstName("Manuel")
                .lastName("Ortega")
                .email("manuelortega@gmail.com")
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        ResultActions response = mockMvc.perform(
                get("/api/v1/employees/{id}", employeeId)
        );

        // Then - Verify the Output
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "404 NOT FOUND" in the Response
                .andExpect(status().isNotFound());
    }

    // Integration Test for Update Employee REST API - Positive Scenario
    @Test
    @DisplayName("Integration Test for Update Employee REST API - Positive Scenario")
    void givenEmployeeForUpdate_whenUpdateEmployee_thenReturnUpdatedEmployeeObject()
            throws Exception {
        // Given - Precondition or Setup
        Employee employee = Employee.builder()
                .firstName("Ivan")
                .lastName("Attal")
                .email("ivanattal@gmail.com")
                .build();
        Employee savedEmployee = employeeRepository.save(employee);

        // Employee From Request
        Employee employeeFromRequest = Employee.builder()
                .firstName("Ivan - UPDATED")
                .lastName("Attal - UPDATED")
                .email("ivanattal.updated@gmail.com")
                .build();

        // When - Action or the Behavior that we are going to test
        ResultActions response = mockMvc.perform(
                put("/api/v1/employees/{id}", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(employeeFromRequest)
                        )
        );

        // Then - Verify the Output
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "201 OK" in the Response
                .andExpect(status().isOk())
                // Test Actual Value with the Expected Value
                .andExpect(jsonPath(
                                "$.firstName",
                                is(employeeFromRequest.getFirstName())
                        )
                )
                .andExpect(jsonPath(
                                "$.lastName",
                                is(employeeFromRequest.getLastName())
                        )
                )
                .andExpect(jsonPath(
                                "$.email",
                                is(employeeFromRequest.getEmail())
                        )
                );
    }

    // Integration Test for Update Employee REST API - Negative Scenario
    @Test
    @DisplayName("Integration Test for Update Employee REST API - Negative Scenario")
    void givenEmployeeForUpdate_whenUpdateEmployee_thenReturn404()
            throws Exception {
        // Given - Precondition or Setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Ivan")
                .lastName("Attal")
                .email("ivanattal@gmail.com")
                .build();
        Employee savedEmployee = employeeRepository.save(employee);

        // Employee From Request
        Employee employeeFromRequest = Employee.builder()
                .firstName("Ivan - UPDATED")
                .lastName("Attal - UPDATED")
                .email("ivanattal.updated@gmail.com")
                .build();

        // When - Action or the Behavior that we are going to test
        ResultActions response = mockMvc.perform(
                put("/api/v1/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(employeeFromRequest)
                        )
        );

        // Then - Verify the Output
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "404 NOT FOUND" in the Response
                .andExpect(status().isNotFound());
    }

    // Integration Test for Delete Employee REST API
    @Test
    @DisplayName("Integration Test for Delete Employee REST API")
    void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // Given - Precondition or Setup
        Employee employee = Employee.builder()
                .firstName("Ivan")
                .lastName("Attal")
                .email("ivanattal@gmail.com")
                .build();
        Employee savedEmployee = employeeRepository.save(employee);

        // When - Action or the Behavior that we are going to test
        ResultActions response = mockMvc.perform(
                delete("/api/v1/employees/{id}", savedEmployee.getId())
        );

        // Then - Verify the Output
        response
                // To Print the Response of the REST API into the Console
                .andDo(print())
                // Verify HTTP Status "200 OK" in the Response
                .andExpect(status().isOk());
    }
}
