package com.dgmf.repository;

import com.dgmf.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmail(String email);
    // Define Custom Query using JPQL with Index Params
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);
    // Define Custom Query using JPQL with Named Params
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND " +
            "e.lastName = :lastName")
    Employee findByJPQLNamedParams(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);
}
