package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 1/26/2018.
 */

public class Menu {
    private String itemName;
    private String itemCuisine;
    private String itemPrice;
    private String vegNonVeg;
    private String availability;
    private String  itemQuantity;

    public Menu(){

    }

    public Menu(String itemCuisine) {
        this.itemCuisine = itemCuisine;
    }

    public String getItemCuisine() {
        return itemCuisine;
    }

    public void setItemCuisine(String itemCuisine) {
        this.itemCuisine = itemCuisine;
    }

    public Menu(String itemName, String itemPrice, String vegNonVeg, String availability, String itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.vegNonVeg = vegNonVeg;
        this.availability = availability;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getVegNonVeg() {
        return vegNonVeg;
    }

    public void setVegNonVeg(String vegNonVeg) {
        this.vegNonVeg = vegNonVeg;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getItemQuantity() {
        if(itemQuantity!=null) {
            return itemQuantity;
        }
        else {
            return "0";
        }
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void incrementQuantity() {
        int qty = Integer.parseInt(itemQuantity);
        qty++;
        this.itemQuantity = qty + "";
    }
}
