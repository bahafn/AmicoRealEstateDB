package com.github.lamico.entities;

public class Apartment {
    private int prNum;
    private int buildingNum;
    private int roomNum;
    private int unitNum;
    private int bedroomNum;
    private int bathroomNum;
    private int livingroomNum;
    private boolean hasBalcony;
    private String kitchenType;
    private boolean hasGarden;

    public Apartment(int prNum, int buildingNum, int roomNum, int unitNum, int bedroomNum, int bathroomNum, int livingroomNum, boolean hasBalcony, String kitchenType, boolean hasGarden) {
        this.prNum = prNum;
        this.buildingNum = buildingNum;
        this.roomNum = roomNum;
        this.unitNum = unitNum;
        this.bedroomNum = bedroomNum;
        this.bathroomNum = bathroomNum;
        this.livingroomNum = livingroomNum;
        this.hasBalcony = hasBalcony;
        this.kitchenType = kitchenType;
        this.hasGarden = hasGarden;
    }

    public int getPrNum() {
        return prNum;
    }

    public void setPrNum(int prNum) {
        this.prNum = prNum;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(int unitNum) {
        this.unitNum = unitNum;
    }

    public int getBedroomNum() {
        return bedroomNum;
    }

    public void setBedroomNum(int bedroomNum) {
        this.bedroomNum = bedroomNum;
    }

    public int getBathroomNum() {
        return bathroomNum;
    }

    public void setBathroomNum(int bathroomNum) {
        this.bathroomNum = bathroomNum;
    }

    public int getLivingroomNum() {
        return livingroomNum;
    }

    public void setLivingroomNum(int livingroomNum) {
        this.livingroomNum = livingroomNum;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public String getKitchenType() {
        return kitchenType;
    }

    public void setKitchenType(String kitchenType) {
        this.kitchenType = kitchenType;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }
}
