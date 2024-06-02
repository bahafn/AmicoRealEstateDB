package com.github.lamico.entities;

import java.util.Date;

public class Employee extends Person {
    private String ePosition;
    private String department;
    private int salary;
    private Date hireDate;

    public Employee(String ssn, String pName, String address, Date dateOfBirth, String bankInfo, String phone,
            String email, String ePosition, String department, int salary, Date hireDate) {
        super(ssn, pName, address, dateOfBirth, bankInfo, phone, email);
        this.ePosition = ePosition;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public String getEPosition() {
        return this.ePosition;
    }

    public void setEPosition(String ePosition) {
        this.ePosition = ePosition;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "salary=" + salary +
                ", hireDate=" + hireDate +
                ", ePosition='" + ePosition + '\'' +
                ", department='" + department + '\'' +
                ", ssn=" + getSsn() +
                '}';
    }
}
