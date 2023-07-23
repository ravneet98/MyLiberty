package com.example.myliberty.Models;

public class account_number {
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public account_number(String number, Boolean registered) {
        this.number = number;
        this.registered = registered;
    }
    public account_number() {
        this.number = number;
        this.registered = registered;
    }

    String number;
    Boolean registered;
}
