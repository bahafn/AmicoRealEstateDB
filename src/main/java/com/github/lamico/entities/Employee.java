package com.github.lamico.entities;

import java.util.Date;

public class Employee {
    private int salary;
    private Date hireDate;
    private String ePosition;
    private String department;
    private String ssn;
    
    public Employee(int salary, Date hireDate, String ePosition, String department, String ssn) {
        this.salary = salary;
        this.hireDate = hireDate;
        this.ePosition = ePosition;
        this.department = department;
        this.ssn = ssn;
    }
    
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getEPosition() {
        return ePosition;
    }

    public void setEPosition(String ePosition) {
        this.ePosition = ePosition;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "salary=" + salary +
                ", hireDate=" + hireDate +
                ", ePosition='" + ePosition + '\'' +
                ", department='" + department + '\'' +
                ", ssn=" + ssn +
                '}';
    }
}

