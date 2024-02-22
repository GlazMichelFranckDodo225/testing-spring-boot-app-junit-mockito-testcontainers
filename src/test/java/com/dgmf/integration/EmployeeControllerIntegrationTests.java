package com.dgmf.integration;

import com.dgmf.entity.Employee;
import com.dgmf.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // To Call REST APIs
public class EmployeeControllerIntegrationTests {
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

}
