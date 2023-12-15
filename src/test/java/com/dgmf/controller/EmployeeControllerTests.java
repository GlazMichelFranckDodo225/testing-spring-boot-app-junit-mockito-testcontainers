package com.dgmf.controller;

import com.dgmf.entity.Employee;
import com.dgmf.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
                .andDo(MockMvcResultHandlers.print())
                // Verify HTTP Status "201 CREATED" in the Response
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // Using JSON Path Method to Check Actual Value ("$.firstName" and so on)
                // with the Expected Value ("employee.getFirstName()" and so on)
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.firstName",
                        CoreMatchers.is(employee.getFirstName())
                    )
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                                "$.lastName",
                                CoreMatchers.is(employee.getLastName())
                        )
                )
                .andExpect(MockMvcResultMatchers.jsonPath(
                                "$.email",
                                CoreMatchers.is(employee.getEmail())
                        )
                )
        ;
    }
}
