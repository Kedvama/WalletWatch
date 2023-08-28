package com.NoviBackend.WalletWatch.request;

import java.util.Date;

public abstract class RequestMessage {
    private String message;
    private Date date;

    public RequestMessage(){
        this.date = new Date();
    }

    public RequestMessage(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
