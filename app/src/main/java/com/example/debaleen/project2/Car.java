package com.example.debaleen.project2;

public class Car {

    private String id, number, owner, type, regDate, insurance, pollution;

    Car(String id, String number, String owner, String type, String regDate, String insurance, String pollution)
    {
        this.id = id;
        this.number = number;
        this.owner = owner;
        this.type = type;
        this.regDate = regDate;
        this.insurance = insurance;
        this.pollution = pollution;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getInsurance() {
        return insurance;
    }

    public String getPollution() {
        return pollution;
    }
}
