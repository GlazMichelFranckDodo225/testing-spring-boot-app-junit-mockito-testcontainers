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
    /* ============== FIELDS ============== */
    // To mock Employee Service
    @MockBean
    private EmployeeService employeeService;
    // To call REST APIs
    @Autowired
    private MockMvc mockMvc;
    // To Serialize and Deserialize Java Objects (Jackson Class)
    @Autowired
    private ObjectMapper objectMapper;

    /* ============== START OF TESTS ============== */
    // JUnit Test for Create Employee REST API
    @Test
    @DisplayName("JUnit Test for Create Employee REST API")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee()
            throws Exception {
        // Given - Precondition or Setup
        Employee employee = Employee.builder()
                .firstName("Xavi")
                .lastName("Disturb")
                .email("xavidisturb@gmail.com")
                .build();

        // To Mock "employeeService.saveEmployee(employee)" Method
        // Presents into the "createEmployee()" Method of the EmployeeController
        // Note ==> Static Import of "BDDMockito.given" Method
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

        // To Mock "employeeService.getAllEmployees()" Method and Prepare
        // a Proper Response for this Method (Stubbing)
        given(employeeService.getAllEmployees()).willReturn(employees);

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

    // JUnit Test for Get Employee By Id REST API
    //  Negative Scenario with No Valid Employee Id
    @Test
    @DisplayName("JUnit Test for Get Employee By Id REST API")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty()
            throws Exception {
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
                .willReturn(Optional.empty());

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

    // JUnit Test for Update Employee REST API - Positive Scenario
    @Test
    @DisplayName("JUnit Test for Update Employee REST API - Positive Scenario")
    void givenEmployeeForUpdate_whenUpdateEmployee_thenReturnUpdatedEmployeeObject()
            throws Exception {
        // Given - Precondition or Setup
        Long employeeId = 3L;
        // Employee From DB
        Employee employeeFromDb = Employee.builder()
                .firstName("Ivan")
                .lastName("Attal")
                .email("ivanattal@gmail.com")
                .build();

        // Employee From Request
        Employee employeeFromRequest = Employee.builder()
                .firstName("Ivan - UPDATED")
                .lastName("Attal - UPDATED")
                .email("ivanattal.updated@gmail.com")
                .build();

        // To Mock "employeeService.getEmployeeById()" Method (Stub Method Call)
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employeeFromDb));

        // To Mock "employeeService.updateEmployee()" Method
        given(employeeService.updateEmployee(
                // Whatever Argument We Pass to Update Employee
                    ArgumentMatchers.any(Employee.class)
                    )
                )
                // This Argument Should Be Simply Return
                .willAnswer(invocation -> invocation.getArgument(0));

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

    // JUnit Test for Update Employee REST API - Negative Scenario
    @Test
    @DisplayName("JUnit Test for Update Employee REST API - Positive Scenario")
    void givenEmployeeForUpdate_whenUpdateEmployee_thenReturn404()
            throws Exception {
        // Given - Precondition or Setup
        Long employeeId = 3L;
        // Employee From DB
        Employee employeeFromDb = Employee.builder()
                .firstName("Ivan")
                .lastName("Attal")
                .email("ivanattal@gmail.com")
                .build();

        // Employee From Request
        Employee employeeFromRequest = Employee.builder()
                .firstName("Ivan - UPDATED")
                .lastName("Attal - UPDATED")
                .email("ivanattal.updated@gmail.com")
                .build();

        // To Mock "employeeService.getEmployeeById()" Method (Stub Method Call)
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        // To Mock "employeeService.updateEmployee()" Method
        given(employeeService.updateEmployee(
                // Whatever Argument We Pass to Update Employee
                    ArgumentMatchers.any(Employee.class)
                    )
                )
                // This Argument Should Be Simply Return
                .willAnswer(invocation -> invocation.getArgument(0));

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
}
