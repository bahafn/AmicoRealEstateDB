package com.github.lamico.entities;

public class Email {
    
    private String address;
    private int ssn;
    
    public Email() {
    }

    public Email(String address, int ssn) {
        this.address = address;
        this.ssn = ssn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        return "Email{" +
                "address='" + address + '\'' +
                ", ssn=" + ssn +
                '}';
    }
}

