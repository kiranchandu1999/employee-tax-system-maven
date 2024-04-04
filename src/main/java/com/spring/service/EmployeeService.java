package com.spring.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.EmployeeTaxDto;
import com.spring.entity.Employee;
import com.spring.repository.EmployeeRepo;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;

    public void addEmployee(Employee emp) {
        // save the employee details into db
        Employee newEmp = employeeRepo.save(emp);

        if(newEmp == null){
            throw new RuntimeException("Not a valid employee details");
        }
    }

    public EmployeeTaxDto getTaxDetailsByEmpId(String empId) throws Exception{
        // get the employee by employee id
        Employee emp = employeeRepo.findById(empId).orElseThrow(() -> new RuntimeException(String.format("Employee with id %s not exists", empId)));

        // if employee is there then need to calculate annual salary and tax and cess amount
        // per day salary
        double perDaySal = emp.getSalaryPM()/30;
        long totalDays = 365;

        // no of days from doj to current year march 31
        LocalDate startDate = emp.getDoj();
        int currentYear = LocalDate.now().getYear();
        int curretMonth = LocalDate.now().getMonthValue();
        if(curretMonth >= 4 && (startDate.getYear() == currentYear && startDate.getMonthValue() >= 4)){
            totalDays = ChronoUnit.DAYS.between(startDate, LocalDate.parse(String.format("%s-03-31", currentYear+1)));
        }
        else if(curretMonth < 4 && (startDate.getYear() == currentYear - 1 && startDate.getMonthValue() >= 4)){
            totalDays = ChronoUnit.DAYS.between(startDate, LocalDate.parse(String.format("%s-03-31", currentYear)));
        }

        double annualSalary = perDaySal * totalDays;
        
        // calculate tax amount
        double tax = 0;
        double temp = annualSalary;
        double taxable = 0;

        // 20% tax slab
        if(temp > 1000000){
            taxable = temp - 1000000;
            tax += taxable * 0.2;
            temp -= taxable;
        }
        // 10% tax slab
        if(temp > 500000){
            taxable = temp - 500000;
            tax += taxable * 0.1;
            temp -= taxable;
        }
        // 5% tax slab
        if(temp > 250000){
            taxable = temp - 250000;
            tax += taxable * 0.05;
            temp -= taxable;
        }

        // claculate cess amount
        double cess = 0;

        if(annualSalary > 2500000){
            taxable = annualSalary - 2500000;
            cess = taxable * 0.02;
        }

        // now create employee tax details object and return
        EmployeeTaxDto empTaxDto = new EmployeeTaxDto();
        empTaxDto.setEmpId(empId);
        empTaxDto.setFirstName(emp.getFirstName());
        empTaxDto.setLastName(emp.getLastName());
        empTaxDto.setYearlySalary(annualSalary);
        empTaxDto.setTax(tax);
        empTaxDto.setCess(cess);

        return empTaxDto;
    }


}
