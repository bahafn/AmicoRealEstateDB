package com.github.lamico.entities;

import java.util.Date;

public class CompanyBroker extends Broker {
    private String ePosition;
    private String department;
    private int salary;
    private Date hireDate;

    public CompanyBroker(String ssn, String pName, String address, Date dateOfBirth, String bankInfo, String phone,
            String email, double share, String ePosition, String department, int salary, Date hireDate) {
        super(ssn, pName, address, dateOfBirth, bankInfo, phone, email, share);
        setEPosition(ePosition);
        setDepartment(department);
        setSalary(salary);
        setHireDate(hireDate);
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
}
