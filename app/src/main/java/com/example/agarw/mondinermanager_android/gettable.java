package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 1/14/2018.
 */

public class gettable {

    String number;
    String callWaiter;
    String confirmStatus;

    public gettable(){
    }
    public gettable(String number, String callWaiter, String confirmStatus) {
        this.number = number;
        this.callWaiter = callWaiter;
        this.confirmStatus = confirmStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCallWaiter() {
        return callWaiter;
    }

    public void setCallWaiter(String callWaiter) {
        this.callWaiter = callWaiter;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

}
