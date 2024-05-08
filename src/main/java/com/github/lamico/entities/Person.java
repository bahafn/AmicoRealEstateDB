package com.github.lamico.entities;

import java.util.Date;

public class Person {
    private String ssn;
    private String pName;
    private String address;
    private Date dateOfBirth;
    private String bankInfo;
    
    public Person(String ssn, String pName, String address, Date dateOfBirth, String bankInfo) {
        this.ssn = ssn;
        this.pName = pName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.bankInfo = bankInfo;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    @Override
    public String toString() {
        return "Person{" +
                "ssn=" + ssn +
                ", pName='" + pName + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", bankInfo='" + bankInfo + '\'' +
                '}';
    }
}
