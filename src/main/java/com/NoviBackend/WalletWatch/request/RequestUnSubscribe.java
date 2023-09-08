package com.NoviBackend.WalletWatch.request;

import java.util.Date;

public class RequestUnSubscribe {
    public String usernameProf;
    private Date date;

    public RequestUnSubscribe() {
        this.date = new Date();
    }

    public RequestUnSubscribe(String username) {
        this();
        this.usernameProf = username;
    }

    public String getUsernameProf() {
        return usernameProf;
    }

    public void setUsernameProf(String usernameProf) {
        this.usernameProf = usernameProf;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
