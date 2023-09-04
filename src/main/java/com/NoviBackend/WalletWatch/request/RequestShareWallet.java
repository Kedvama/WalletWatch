package com.NoviBackend.WalletWatch.request;

import java.util.Date;

public class RequestShareWallet {
    private boolean shareWallet;
    private Date date;

    public RequestShareWallet(){
        this.date = new Date();
    }

    public RequestShareWallet(boolean shareWallet) {
        this();
        this.shareWallet = shareWallet;
    }

    public boolean getShareWallet() {
        return shareWallet;
    }

    public void setShareWallet(boolean shareWallet) {
        this.shareWallet = shareWallet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

