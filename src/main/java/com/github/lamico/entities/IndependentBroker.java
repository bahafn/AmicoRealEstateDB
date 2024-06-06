package com.github.lamico.entities;

import java.util.Date;

public class IndependentBroker extends Broker {
    private int commission;

    public IndependentBroker(String ssn, String pName, String address, Date dateOfBirth, String bankInfo, String phone,
            String email, double share, int commission) {
        super(ssn, pName, address, dateOfBirth, bankInfo, phone, email, share);
        setCommission(commission);
    }

    public int getCommission() {
        return this.commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }
}
