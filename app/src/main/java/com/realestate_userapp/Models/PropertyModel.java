package com.realestate_userapp.Models;

public class PropertyModel {
    private String propertyId;
    private String propertyName;
    private String propertyType;
    private String area;
    private String country;
    private String rooms;
    private String floors;
    private String parking;
    private String nearbyPlace;
    private String rent;
    private String deposit;
    private String roomType;
    private String saleType;
    private String city;
    private String brokerid;
    private String gym;
    private Long status;
    private String percentageOfCommission;

    // Default constructor (required by Firebase)
    public PropertyModel() {
    }

    public PropertyModel(String propname, String rent, String saletype, String rooms, String roomtype, String proptype, String parking, String nearby, String floors, String deposit, String country, String city, String brokerid, String area, String propertyId, String gym, String percentageOfCommission) {
        this.propertyName = propname;
        this.rent = rent;
        this.saleType = saletype;
        this.rooms = rooms;
        this.roomType = roomtype;
        this.propertyType = proptype;
        this.parking = parking;
        this.nearbyPlace = nearby;
        this.floors = floors;
        this.deposit = deposit;
        this.country = country;
        this.city = city;
        this.brokerid = brokerid;
        this.area = area;
        this.propertyId = propertyId;
        this.gym = gym;
        this.percentageOfCommission = percentageOfCommission;
    }

    public PropertyModel(String name, String amount, String area, String city, String propertyId) {
        this.propertyName = name;
        this.rent = amount;
        this.area = area;
        this.city = city;
        this.propertyId = propertyId;
    }

    public PropertyModel(String propname, String propid, String brokerid) {
        this.propertyName = propname;
        this.propertyId = propid;
        this.brokerid = brokerid;
    }

    // Parameterized constructor
//    public Property(String propertyName, String propertyType, String area, String country,
//                    String rooms, String floors, String parking, String nearbyPlace,
//                    String rent, String deposit, String roomType, String saleType) {
//        this.propertyName = propertyName;
//        this.propertyType = propertyType;
//        this.area = area;
//        this.country = country;
//        this.rooms = rooms;
//        this.floors = floors;
//        this.parking = parking;
//        this.nearbyPlace = nearbyPlace;
//        this.rent = rent;
//        this.deposit = deposit;
//        this.roomType = roomType;
//        this.saleType = saleType;
//    }

    // Getters and setters for all fields
    // You can generate these using your IDE or write them manually

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getNearbyPlace() {
        return nearbyPlace;
    }

    public void setNearbyPlace(String nearbyPlace) {
        this.nearbyPlace = nearbyPlace;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBrokerid() {
        return brokerid;
    }

    public void setBrokerid(String brokerid) {
        this.brokerid = brokerid;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getPercentageOfCommission() {
        return percentageOfCommission;
    }

    public void setPercentageOfCommission(String percentageOfCommission) {
        this.percentageOfCommission = percentageOfCommission;
    }
}

