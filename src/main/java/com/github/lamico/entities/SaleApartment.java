package com.github.lamico.entities;

public class SaleApartment extends Apartment {

	public SaleApartment(int prNum, int buildingNum, int roomNum, int unitNum, int bedroomNum, int bathroomNum,
			int livingroomNum, boolean hasBalcony, String kitchenType, boolean hasGarden, double price) {
		super(prNum, buildingNum, roomNum, unitNum, bedroomNum, bathroomNum, livingroomNum, hasBalcony, kitchenType,
				hasGarden);
		this.price = price;
	}

	private Apartment apartment;
	private double price;

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
