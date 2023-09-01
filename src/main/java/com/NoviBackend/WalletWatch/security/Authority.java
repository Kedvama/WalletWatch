package com.NoviBackend.WalletWatch.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name="authorities")
public class Authority implements Serializable {

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String authority;

    public Authority() {
    }

    public Authority(String username){
        this.username = username;
    }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
