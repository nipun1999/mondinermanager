package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 1/18/2018.
 */

public class getdishdetails {
    String id;
    String price;
    String name;
    int quantity;



    int total;


    public getdishdetails(){

    }


    public getdishdetails(String id, String price, int quantity, String name, int total) {

        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.total = total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
