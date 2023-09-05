package com.NoviBackend.WalletWatch.request;

import java.util.Date;

public class RequestSubscribe {
    public String subscribeToUsername;
    private Date date;

    public RequestSubscribe() {
        this.date = new Date();
    }

    public RequestSubscribe(String username) {
        this.subscribeToUsername = username;
        this.date = new Date();
    }

    public String getSubscribeToUsername() {
        return subscribeToUsername;
    }

    public void setSubscribeToUsername(String subscribeToUsername) {
        this.subscribeToUsername = subscribeToUsername;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
