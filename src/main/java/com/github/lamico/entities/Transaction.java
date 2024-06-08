package com.github.lamico.entities;

import java.sql.Date;


public class Transaction {
    private String ID;
    private Date paymentDate;
    private double amount;
    private String recipient;
    private String sender;
    private String contractNo;
    
    
	public Transaction(String ID, Date paymentDate, double amount, String recipient, String sender,
			String contractNo) {
		this.ID = ID;
		this.paymentDate = paymentDate;
		this.amount = amount;
		this.recipient = recipient;
		this.sender = sender;
		this.contractNo = contractNo;
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

   
}