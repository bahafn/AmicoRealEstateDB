package com.github.lamico.entities;

public class Building extends RealEstate {
	private int landNum;
	private String bName;
	private int yearBuilt;
	private int floorNum;

	public Building(int prNum, String prCondition, String city, String streetName, double valuation,
			String areaDescription, double area, String ownerSSN, int landNum, String bName, int yearBuilt,
			int floorNum) {
		super(prNum, prCondition, city, streetName, valuation, areaDescription, area, ownerSSN);
		this.landNum = landNum;
		this.bName = bName;
		this.yearBuilt = yearBuilt;
		this.floorNum = floorNum;
	}

	public Building(int prNum, String bName, int floorNum) {
		this.setPrNum(prNum);
		this.bName = bName;
		this.floorNum = floorNum;
	}

	public int getLandNum() {
		return landNum;
	}

	public void setBName(String bName) {
		this.bName = bName;
	}

	public int getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(int yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public int getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}

	public String getBName() {
		return bName;
	}

	public void setLandNum(int landNum) {
		this.landNum = landNum;
	}

}
