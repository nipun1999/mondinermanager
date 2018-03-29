package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 2/17/2018.
 */

public class getCallWaiter {
    String number;
    String tableid;

    public getCallWaiter(String number, String tableid) {
        this.number = number;
        this.tableid = tableid;
    }

    public getCallWaiter() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

}
