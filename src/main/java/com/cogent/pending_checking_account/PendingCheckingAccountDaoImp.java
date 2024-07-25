package com.cogent.pending_checking_account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PendingCheckingAccountDaoImp {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private int customerId;
    private int startingBalance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(int startingBalance) {
        this.startingBalance = startingBalance;
    }

    @Override
    public String toString() {
        return "Checking Account Request :" +
                "id=" + id +
                ", customerId=" + customerId +
                ", startingBalance=" + startingBalance;
    }
}
