package com.example.myliberty.Models;

public class MobilePlan {
    Integer PlanId;
    String PlanName;
    Integer Cost;
    Integer Speed;
    Integer PlanData;
    public Integer getPlanId() {
        return PlanId;
    }

    public void setPlanId(Integer planId) {
        PlanId = planId;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public Integer getCost() {
        return Cost;
    }

    public void setCost(Integer cost) {
        Cost = cost;
    }

    public Integer getSpeed() {
        return Speed;
    }

    public void setSpeed(Integer speed) {
        Speed = speed;
    }

    public Integer getPlanData() {
        return PlanData;
    }

    public void setPlanData(Integer planData) {
        PlanData = planData;
    }

    public MobilePlan(Integer planId, String planName, Integer cost, Integer speed, Integer planData) {
        PlanId = planId;
        PlanName = planName;
        Cost = cost;
        Speed = speed;
        PlanData = planData;
    }


}
