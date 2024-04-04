package com.spring.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.EmployeeTaxDto;
import com.spring.entity.Employee;
import com.spring.service.EmployeeService;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@Valid @RequestBody Employee emp){
        employeeService.addEmployee(emp);
        return new ResponseEntity<>("New Employee added", HttpStatus.CREATED);
    }

    @GetMapping("/get-tax-details/{empId}")
    public ResponseEntity<EmployeeTaxDto> getTaxDetailsByEmpId(@PathVariable String empId) throws Exception{
        EmployeeTaxDto dto = employeeService.getTaxDetailsByEmpId(empId);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
