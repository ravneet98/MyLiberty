package com.example.myliberty.Models;

public class Plan {

    String planId;
    String planName;
    String planCost;
    String planSpeed;
    String planData;
    String planType;

    public Plan(String planId, String planName, String planCost, String planSpeed, String planData, String planType) {
        this.planId = planId;
        this.planName = planName;
        this.planCost = planCost;
        this.planSpeed = planSpeed;
        this.planData = planData;
        this.planType = planType;
    }
    public Plan(){

    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanCost() {
        return planCost;
    }

    public void setPlanCost(String planCost) {
        this.planCost = planCost;
    }

    public String getPlanSpeed() {
        return planSpeed;
    }

    public void setPlanSpeed(String planSpeed) {
        this.planSpeed = planSpeed;
    }

    public String getPlanData() {
        return planData;
    }

    public void setPlanData(String planData) {
        this.planData = planData;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}
