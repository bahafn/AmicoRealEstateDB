package com.github.lamico.entities;

import java.util.Date;

public class Broker extends Person {
    private double share;

    public Broker(String ssn, String pName, String address, Date dateOfBirth, String bankInfo, String phone,
            String email, double share) {
        super(ssn, pName, address, dateOfBirth, bankInfo, phone, email);
        setShare(share);
    }

    public double getShare() {
        return this.share;
    }

    public void setShare(double share) {
        this.share = share;
    }
}
