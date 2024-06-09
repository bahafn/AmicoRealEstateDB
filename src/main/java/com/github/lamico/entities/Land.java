package com.github.lamico.entities;

public class Land extends RealEstate {

	private int plotNum;
	private int blockNum;

	public Land(int prNum, String prCondition, String city, String streetName, double valuation, String areaDescription,
			double area, String ownerSSN, int plotNum, int blockNum) {
		super(prNum, prCondition, city, streetName, valuation, areaDescription, area, ownerSSN);
		this.plotNum = plotNum;
		this.blockNum = blockNum;
	}

	public int getPlotNum() {
		return plotNum;
	}

	public void setPlotNum(int plotNum) {
		this.plotNum = plotNum;
	}

	public int getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

}
