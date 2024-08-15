package com.sushi.api.common;

import com.sushi.api.model.Employee;
import com.sushi.api.model.dto.employee.EmployeeRequestDTO;
import com.sushi.api.model.dto.employee.EmployeeUpdateDTO;

import java.util.List;
import java.util.UUID;

import static com.sushi.api.common.CustomerConstants.EMAIL;
import static com.sushi.api.common.CustomerConstants.PASSWORD;

public class EmployeeConstants {
    public static final Employee EMPLOYEE_LOGIN = new Employee(UUID.randomUUID(), "ana", EMAIL, PASSWORD);
    public static final Employee EMPLOYEE = new Employee(UUID.randomUUID(), "isabel", "isabel@gmail.com", "1234");
    public static final Employee EMPLOYEE2 = new Employee(UUID.randomUUID(), "joao", "joao@gmail.com", "1234");
    public static final Employee EMPLOYEE3 = new Employee(UUID.randomUUID(), "maria", "maria@gmail.com", "1234");
    public static final Employee EMPLOYEE4 = new Employee(UUID.randomUUID(), "mariana", "mariana@gmail.com", "1234");
    public static final List<Employee> EMPLOYEES = List.of(EMPLOYEE2, EMPLOYEE3, EMPLOYEE4);
    public static final EmployeeRequestDTO EMPLOYEE_REQUEST_DTO = new EmployeeRequestDTO("isabel", "isabel@gmail.com", "1234");
    public static final EmployeeUpdateDTO EMPLOYEE_UPDATE_DTO = new EmployeeUpdateDTO(UUID.randomUUID(), "isabel", "isabel@gmail.com", "1234");
}