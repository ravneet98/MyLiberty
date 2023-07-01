package com.example.myliberty.Models;

public class SupportQueries {
    String qid;
    String query;
    String solution;

    String createdDate;
    String solutionDate;
    String status;
    String requestedBy;

    public SupportQueries(String qid, String query, String solution, Boolean isSolved, String createdDate, String solutionDate, String status, String requestedBy) {
        this.qid = qid;
        this.query = query;
        this.solution = solution;

        this.createdDate = createdDate;
        this.solutionDate = solutionDate;
        this.status = status;
        this.requestedBy = requestedBy;
    }

    public SupportQueries() {

    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }


    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(String solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
