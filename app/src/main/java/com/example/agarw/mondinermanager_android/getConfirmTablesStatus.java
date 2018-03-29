package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 2/17/2018.
 */

public class getConfirmTablesStatus {
    String number;
    String tablekey;

    public String getTablekey() {
        return tablekey;
    }

    public void setTablekey(String tablekey) {
        this.tablekey = tablekey;
    }

    public getConfirmTablesStatus(String number, String tablekey) {
        this.number = number;
        this.tablekey = tablekey;
    }


    public getConfirmTablesStatus(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }




}
