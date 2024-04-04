package com.spring.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmployeeTaxDto {
    private String empId;

    private String firstName;

    private String lastName;

    private double yearlySalary;

    private double tax;

    private double cess;
}
