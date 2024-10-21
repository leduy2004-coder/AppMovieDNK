package com.example.appmoviednk;


import com.example.appmoviednk.model.CustomerModel;


public class UserSession {
    private static UserSession instance;
    private CustomerModel loggedInAccount;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInAccount(CustomerModel account) {
        this.loggedInAccount = account;
    }

    public CustomerModel getLoggedInAccount() {
        return loggedInAccount;
    }

    public void clearSession() {
        loggedInAccount = null;
    }
}
