package com.dgmf.controller;

import com.dgmf.entity.Employee;
import com.dgmf.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {
    // To call REST API
    @Autowired
    private MockMvc mockMvc;
    // To mock Employee Service
    @MockBean
    private EmployeeService employeeService;
    // To Serialize and Deserialize Java Objects
    @Autowired
    private ObjectMapper objectMapper;

    // JUnit Test for Create Employee REST API
    @Test
    @DisplayName("JUnit Test for Create Employee REST API")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee()
            throws Exception {
        // Given - Precondition or Setup
        Employee employee = Employee.builder()
                .firstName("Manuel")
                .lastName("Ortega")
                .email("manuelortega@gmail.com")
                .build();

        // To Stub (to Mock) "saveEmployee()" Method
        // of "Employee Service" (Static Import)
        // "any()" Method ==> Ambiguity with Static Import
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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

    // JUnit Test for Get All Employees REST API
    @Test
    @DisplayName("JUnit Test for Get All Employees REST API")
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList()
            throws Exception {
        // Given - Precondition or Setup
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder()
                        .firstName("Gillian")
                        .lastName("Barré")
                        .email("gillianbarre@gmail.com")
                        .build());

        employees.add(Employee.builder()
                        .firstName("Tony")
                        .lastName("Stark")
                        .email("tonystark@gmail.com")
                        .build());

        // To Stub "employeeService.getAllEmployees()" Method and Prepare
        // a Proper Response for this Method
        given(employeeService.getAllEmployees()).willReturn(employees);

        // When - Action or the Behavior that we are going to test
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

    // JUnit Test for Get Employee By Id REST API
    //  Positive Scenario with Valid Employee Id
    @Test
    @DisplayName("JUnit Test for Get Employee By Id REST API")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // Given - Precondition or Setup
        // Given Id
        Long employeeId = 1L;
        // Employee to Recover
        Employee employee = Employee.builder()
                .firstName("Manuel")
                .lastName("Ortega")
                .email("manuelortega@gmail.com")
                .build();

        // To Mock "employeeService.getEmployeeById()" Method
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));

        // When - Action or the Behavior that we are going to test
        ResultActions response = mockMvc.perform(get(
                "/api/v1/employees/{id}",
                employeeId
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
}
