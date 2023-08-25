package com.NoviBackend.WalletWatch.request;


public class RequestPromote {

    private String company;
    private String introduction;

    public RequestPromote() {
    }

    public RequestPromote(String company, String introduction){
        this.company = company;
        this.introduction = introduction;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
