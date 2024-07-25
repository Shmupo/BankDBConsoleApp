package com.cogent.checking_account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CheckingAccountDaoImp implements CheckingAccountDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountNumber;
    private int balance;
    private int customerId;

    @Override
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void setBalance(int amount) {
        this.balance = amount;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Checking Account: " +
                "accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", customerId=" + customerId;
    }
}
