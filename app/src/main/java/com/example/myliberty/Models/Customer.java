package com.example.myliberty.Models;

public class Customer {

public Customer(){

}
    public String accountNumber;
    public String name;
    public String email;
    public String planId;
    public String mobileNumber;
    public String cycleStartDate;
    public String cycleEndDate;
    public Boolean billPaid;
    public Float dataRemaining;
    public Float maxData;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(String cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public String getCycleEndDate() {
        return cycleEndDate;
    }

    public void setCycleEndDate(String cycleEndDate) {
        this.cycleEndDate = cycleEndDate;
    }

    public Boolean getBillPaid() {
        return billPaid;
    }

    public void setBillPaid(Boolean billPaid) {
        this.billPaid = billPaid;
    }

    public Float getDataRemaining() {
        return dataRemaining;
    }

    public void setDataRemaining(Float dataRemaining) {
        this.dataRemaining = dataRemaining;
    }

    public Float getMaxData() {
        return maxData;
    }

    public void setMaxData(Float maxData) {
        this.maxData = maxData;
    }

    public Customer(String accountNumber, String name, String email, String planId, String mobileNumber, String cycleStartDate, String cycleEndDate, Boolean billPaid, Float dataRemaining, Float maxData) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.planId = planId;
        this.mobileNumber = mobileNumber;
        this.cycleStartDate = cycleStartDate;
        this.cycleEndDate = cycleEndDate;
        this.billPaid = billPaid;
        this.dataRemaining = dataRemaining;
        this.maxData = maxData;
    }
    public Customer(String planId, String cycleStartDate, String cycleEndDate, Boolean billPaid, Float dataRemaining, Float maxData) {
        this.planId = planId;
        this.cycleStartDate = cycleStartDate;
        this.cycleEndDate = cycleEndDate;
        this.billPaid = billPaid;
        this.dataRemaining = dataRemaining;
        this.maxData = maxData;
    }
}
