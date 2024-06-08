package com.github.lamico.entities;

import java.sql.Date;

public class Contract {
    private String contractNo;
    private Date cDate;
    private String cStatus;
    private String arrangmentType;
    private double price;
    private String brokerSSN;
    private String clientSSN;
    private String prNum;

    public Contract(String contractNo, Date cDate, String cStatus, String arrangmentType, double price, 
                    String brokerSSN, String clientSSN, String prNum) {
        this.contractNo = contractNo;
        this.cDate = cDate;
        this.cStatus = cStatus;
        this.arrangmentType = arrangmentType;
        this.price = price;
        this.brokerSSN = brokerSSN;
        this.clientSSN = clientSSN;
        this.prNum = prNum;
    }

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Date getcDate() {
		return cDate;
	}

	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String cStatus) {
		this.cStatus = cStatus;
	}

	public String getArrangmentType() {
		return arrangmentType;
	}

	public void setArrangmentType(String arrangmentType) {
		this.arrangmentType = arrangmentType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBrokerSSN() {
		return brokerSSN;
	}

	public void setBrokerSSN(String brokerSSN) {
		this.brokerSSN = brokerSSN;
	}

	public String getClientSSN() {
		return clientSSN;
	}

	public void setClientSSN(String clientSSN) {
		this.clientSSN = clientSSN;
	}

	public String getPrNum() {
		return prNum;
	}

	public void setPrNum(String prNum) {
		this.prNum = prNum;
	}

   

}
