package com.github.lamico.entities;

public class Phone {
    
    private String phoneNumber;
    private int ssn;
    
    public Phone() {
    }

    public Phone(String phoneNumber, int ssn) {
        this.phoneNumber = phoneNumber;
        this.ssn = ssn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", ssn=" + ssn +
                '}';
    }
}
