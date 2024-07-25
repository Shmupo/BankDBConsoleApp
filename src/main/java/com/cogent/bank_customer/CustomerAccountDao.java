package com.cogent.bank_customer;

interface CustomerAccountDao {
    int getId();
    void setId(int id);
    String getUser();
    void setUser(String user);
    String getPass();
    void setPass(String pass);
}
