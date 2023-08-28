package com.NoviBackend.WalletWatch.request;


public class RequestPromote extends RequestMessage{

    private String company;
    private String introduction;

    public RequestPromote(){
    }

    public RequestPromote(String company, String introduction, String message){
        super(message);
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
