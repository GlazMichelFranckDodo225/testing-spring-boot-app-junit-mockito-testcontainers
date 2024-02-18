package com.dgmf.integration;

import com.dgmf.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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

}
