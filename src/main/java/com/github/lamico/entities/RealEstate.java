package com.github.lamico.entities;

public class RealEstate {
	private int prNum;
	private String prCondition;
	private String city;
	private String streetName;
	private double valuation;
	private String areaDescription;
	private double area;
	private String ownerSSN;

	public RealEstate(int prNum, String prCondition, String city, String streetName, double valuation,
			String areaDescription, double area, String ownerSSN) {
		this.prNum = prNum;
		this.prCondition = prCondition;
		this.city = city;
		this.streetName = streetName;
		this.valuation = valuation;
		this.areaDescription = areaDescription;
		this.area = area;
		this.ownerSSN = ownerSSN;
	}

	public RealEstate() {
	}

	public int getPrNum() {
		return prNum;
	}

	public void setPrNum(int prNum) {
		this.prNum = prNum;
	}

	public String getPrCondition() {
		return prCondition;
	}

	public void setPrCondition(String prCondition) {
		this.prCondition = prCondition;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public double getValuation() {
		return valuation;
	}

	public void setValuation(double valuation) {
		this.valuation = valuation;
	}

	public String getAreaDescription() {
		return areaDescription;
	}

	public void setAreaDescription(String areaDescription) {
		this.areaDescription = areaDescription;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public String getOwnerSSN() {
		return ownerSSN;
	}

	public void setOwnerSSN(String ownerSSN) {
		this.ownerSSN = ownerSSN;
	}
}
