package com.cogent.checking_account;

interface CheckingAccountDao {
    public void setAccountNumber(int accountNumber);
    int getAccountNumber();
    void setBalance(int balance);
    int getBalance();
    public int getCustomerId();
    public void setCustomerId(int customerId);
}
