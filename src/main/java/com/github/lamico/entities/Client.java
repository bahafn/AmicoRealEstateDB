package com.github.lamico.entities;

import java.util.Date;

public class Client extends Person {
    private String sponsor;
    private int incomeLevel;
    private String employer;

    public Client(String ssn, String pName, String address, Date dateOfBirth, String bankInfo, String phone,
            String email, String sponsor, int incomeLevel, String employer) {
        super(ssn, pName, address, dateOfBirth, bankInfo, phone, email);
        setSponsor(sponsor);
        setIncomeLevel(incomeLevel);
        setEmployer(employer);
    }

    public String getSponsor() {
        return this.sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public int getIncomeLevel() {
        return this.incomeLevel;
    }

    public void setIncomeLevel(int incomeLevel) {
        this.incomeLevel = incomeLevel;
    }

    public String getEmployer() {
        return this.employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }
}
