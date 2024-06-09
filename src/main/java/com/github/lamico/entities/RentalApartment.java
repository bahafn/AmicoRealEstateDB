package com.github.lamico.entities;

public class RentalApartment extends Apartment {
	public RentalApartment(int prNum, int buildingNum, int roomNum, int unitNum, int bedroomNum, int bathroomNum,
			int livingroomNum, boolean hasBalcony, String kitchenType, boolean hasGarden, double rent) {
		super(prNum, buildingNum, roomNum, unitNum, bedroomNum, bathroomNum, livingroomNum, hasBalcony, kitchenType,
				hasGarden);
		this.setRent(rent);
	}

	private Apartment apartment;
	private double rent;

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

}
