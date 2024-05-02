package com.dgmf.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {
    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        // Deploying MySQL DB in Docker Container
        // This Test Container Will Be Shared Between Test Methods
        // @Testcontainers Annotation Will Manage the Life Cycle of
        // this Container
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                // To Configure Testcontainers Database Properties
                .withDatabaseName("ems")
                .withUsername("username")
                .withPassword("12345");

        // To Start the MySQL Container
        MY_SQL_CONTAINER.start();
    }

    // Deploying MySQL DB in Docker Container
    // This Test Container Will Be Shared Between Test Methods
    // @Testcontainers Annotation Will Manage the Life Cycle of
    // this Container
    /*@Container
    private static final MySQLContainer MySQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    // To Configure Testcontainers Database Properties
                    .withDatabaseName("ems")
                    .withUsername("username")
                    .withPassword("12345");*/

    // To Link MySQL Docker Container with ApplicationContext and
    // Be Able to Dynamically Fetch Value from MySQL Container in
    // Order to Add that Value into ApplicationContext
    @DynamicPropertySource
    public static void dynamicPropertySource(
            // To Registry Username and Password in this Class
            DynamicPropertyRegistry dynamicPropertyRegistry
    ) {
        // Fetch Values from MySQL Container and
        // Add to the "dynamicPropertyRegistry" (or ApplicationContext)
        // Key Comes from "application.properties" file
        dynamicPropertyRegistry.add(
                "spring.datasource.url",
                MY_SQL_CONTAINER::getJdbcUrl
        );
        // Add Username and Password
        dynamicPropertyRegistry.add(
                "spring.datasource.username",
                MY_SQL_CONTAINER::getUsername
        );
        dynamicPropertyRegistry.add(
                "spring.datasource.password",
                MY_SQL_CONTAINER::getPassword
        );
    }


}
