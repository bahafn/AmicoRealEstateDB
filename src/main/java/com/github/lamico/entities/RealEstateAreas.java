package com.github.lamico.entities;

public class RealEstateAreas {
	private String city;
	private double avgPrice;
	private int sellerCount;

	public RealEstateAreas(String city, double avgPrice, int sellerCount) {
		this.city = city;
		this.avgPrice = avgPrice;
		this.sellerCount = sellerCount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public int getSellerCount() {
		return sellerCount;
	}

	public void setSellerCount(int sellerCount) {
		this.sellerCount = sellerCount;
	}

}
