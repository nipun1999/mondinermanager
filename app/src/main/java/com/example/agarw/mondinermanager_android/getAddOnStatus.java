package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 3/13/2018.
 */

public class getAddOnStatus {
    String name;
    String category;
    String addOn;

    public getAddOnStatus(){

    }
    public getAddOnStatus(String name, String category, String addOn) {
        this.name = name;
        this.category = category;
        this.addOn = addOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddOn() {
        return addOn;
    }

    public void setAddOn(String addOn) {
        this.addOn = addOn;
    }


}
