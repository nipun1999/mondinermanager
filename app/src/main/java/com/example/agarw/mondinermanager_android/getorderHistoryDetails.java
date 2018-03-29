package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 3/13/2018.
 */

public class getorderHistoryDetails {
    String name;
    String price;
    int quantity;
    public getorderHistoryDetails(){

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public getorderHistoryDetails(String name, String price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }



}
