package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 1/17/2018.
 */

public class getbill {
    String name;
    String price;

    String dishID;
    Integer quantity;

    public getbill(){

    }

    public getbill(String name, String price, Integer quantity,String dishID) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.dishID = dishID;
    }

    public String getDishID() {
        return dishID;
    }

    public void setDishID(String dishID) {
        this.dishID = dishID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
